package microapp.tag.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerMapperTest {

    private ServerMapper serverMapper;

    @BeforeEach
    public void setUp() {
        serverMapper = new ServerMapperImpl();
    }
}
