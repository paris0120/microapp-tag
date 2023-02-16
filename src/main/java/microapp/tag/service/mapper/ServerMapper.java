package microapp.tag.service.mapper;

import microapp.tag.domain.ServerTag;
import microapp.tag.service.dto.ServerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServerTag} and its DTO {@link ServerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServerMapper extends EntityMapper<ServerDTO, ServerTag> {}
