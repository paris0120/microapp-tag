package microapp.tag.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.tag.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagTag.class);
        TagTag tagTag1 = new TagTag();
        tagTag1.setId(1L);
        TagTag tagTag2 = new TagTag();
        tagTag2.setId(tagTag1.getId());
        assertThat(tagTag1).isEqualTo(tagTag2);
        tagTag2.setId(2L);
        assertThat(tagTag1).isNotEqualTo(tagTag2);
        tagTag1.setId(null);
        assertThat(tagTag1).isNotEqualTo(tagTag2);
    }
}
