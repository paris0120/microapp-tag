package microapp.tag.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.tag.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParentTypeDTO.class);
        ParentTypeDTO parentTypeDTO1 = new ParentTypeDTO();
        parentTypeDTO1.setId(1L);
        ParentTypeDTO parentTypeDTO2 = new ParentTypeDTO();
        assertThat(parentTypeDTO1).isNotEqualTo(parentTypeDTO2);
        parentTypeDTO2.setId(parentTypeDTO1.getId());
        assertThat(parentTypeDTO1).isEqualTo(parentTypeDTO2);
        parentTypeDTO2.setId(2L);
        assertThat(parentTypeDTO1).isNotEqualTo(parentTypeDTO2);
        parentTypeDTO1.setId(null);
        assertThat(parentTypeDTO1).isNotEqualTo(parentTypeDTO2);
    }
}
