package microapp.tag.service;

import java.util.*;
import microapp.tag.domain.KafkaCmd;
import microapp.tag.repository.TagRepository;
import microapp.tag.service.dto.TagDTO;
import microapp.tag.service.mapper.TagMapper;
import microapp.tag.web.rest.TagResource;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaService implements InitializingBean {

    @Value(value = "${spring.cloud.stream.kafka.binder.brokers:localhost:9092}")
    private String kafkaAddress;

    @Value(value = "${spring.application.name:tag}")
    private String serverName;

    private HashMap<String, String[]> serverTypes;
    private HashSet<String> topics;

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    KafkaTemplate<String, Object> template;

    @Autowired
    TagRepository tagRepository;

    private final TagMapper tagMapper;

    public KafkaService(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
        this.serverTypes = new HashMap<>();
        serverTypes.put("TICKET", new String[] { "TICKET" });
        serverTypes.put("Comments", new String[] { "Comment", "Questions" });
    }

    public ListenableFuture<SendResult<String, Object>> send(String topic, Object message) {
        System.out.println("Sent Message in group: " + message);
        return template.send(topic, message);
    }

    public void createTopic(String topicName) {
        topicName = serverName.toUpperCase() + "@" + topicName.toUpperCase();
        if (topics.add(topicName)) new NewTopic(topicName, 1, (short) 1);
    }

    public String getProducerTopic(KafkaCmd cmd) {
        return new StringBuilder()
            .append(serverName.toUpperCase())
            .append("@")
            .append(cmd.getRequestServerName())
            .append("_")
            .append(String.join("_", cmd.getCmds()))
            .toString()
            .toUpperCase();
    }

    public String getConsumoerTopic(KafkaCmd cmd) {
        return new StringBuilder()
            .append(cmd.getRequestServerName())
            .append("@")
            .append(serverName.toUpperCase())
            .append("_")
            .append(String.join("_", cmd.getCmds()))
            .toString()
            .toUpperCase();
    }

    @KafkaListener(topics = "TAG_CMD", groupId = "TAGSERVER")
    public void listenRequest(KafkaCmd cmd) {
        if (cmd.getRequestServerName() == null) {
            this.log.info("Request server name not available.");
            return;
        }
        if (cmd.getCmds() == null || cmd.getCmds().length == 0) {
            this.log.info("CMD not available.");
            return;
        }
        String topic = getProducerTopic(cmd);
        if (!topics.contains(topic)) {
            log.info("Invalid topic:", topic);
        }
        log.info("To refresh topic:", topic);
        switch (cmd.getCmds()[0]) { //command
            case "TYPES": //sent types
                if (cmd.getCmds().length > 1) serverTypes.put(cmd.getRequestServerName(), cmd.getCmds()[1].split(","));
                break;
            case "TAGS": //request tags
                if (cmd.getCmds().length > 1) {
                    tagRepository
                        .findAllByParentServerAndParentTypeOrderByTagAsc(cmd.getRequestServerName(), cmd.getCmds()[1])
                        .collectList()
                        .subscribe(tags -> template.send(topic, tags));
                } else tagRepository
                    .findAllByParentServerOrderByTagAsc(cmd.getRequestServerName())
                    .collectList()
                    .subscribe(tags -> template.send(topic, tags));
                break;
        }
    }

    public void sendCmd(String targetServerName, String[] cmds) {
        template.send(targetServerName.toUpperCase() + "_CMD", new KafkaCmd(serverName.toUpperCase(), cmds));
    }

    public HashMap<String, String[]> getServerTypes() {
        return serverTypes;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        topics = new HashSet<>();
    }
}
