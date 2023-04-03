package microapp.tag.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.tag.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServerTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServerTag.class);
        ServerTag serverTag1 = new ServerTag();
        serverTag1.setId(1L);
        ServerTag serverTag2 = new ServerTag();
        serverTag2.setId(serverTag1.getId());
        assertThat(serverTag1).isEqualTo(serverTag2);
        serverTag2.setId(2L);
        assertThat(serverTag1).isNotEqualTo(serverTag2);
        serverTag1.setId(null);
        assertThat(serverTag1).isNotEqualTo(serverTag2);
    }
}
