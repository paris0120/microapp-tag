package microapp.tag.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParentTypeMapperTest {

    private ParentTypeMapper parentTypeMapper;

    @BeforeEach
    public void setUp() {
        parentTypeMapper = new ParentTypeMapperImpl();
    }
}
