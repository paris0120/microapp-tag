package microapp.tag.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.tag.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParentTypeTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParentTypeTag.class);
        ParentTypeTag parentTypeTag1 = new ParentTypeTag();
        parentTypeTag1.setId(1L);
        ParentTypeTag parentTypeTag2 = new ParentTypeTag();
        parentTypeTag2.setId(parentTypeTag1.getId());
        assertThat(parentTypeTag1).isEqualTo(parentTypeTag2);
        parentTypeTag2.setId(2L);
        assertThat(parentTypeTag1).isNotEqualTo(parentTypeTag2);
        parentTypeTag1.setId(null);
        assertThat(parentTypeTag1).isNotEqualTo(parentTypeTag2);
    }
}
