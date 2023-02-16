package microapp.tag.domain;

import java.io.Serializable;

public class KafkaCmd implements Serializable {

    String requestServerName;

    String[] cmds;

    public KafkaCmd() {}

    public KafkaCmd(String requestServerName, String[] cmds) {
        this.requestServerName = requestServerName;
        this.cmds = cmds;
    }

    public String getRequestServerName() {
        return requestServerName;
    }

    public void setRequestServerName(String requestServerName) {
        this.requestServerName = requestServerName;
    }

    public String[] getCmds() {
        return cmds;
    }

    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }
}
