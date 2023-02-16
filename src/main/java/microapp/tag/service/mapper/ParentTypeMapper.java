package microapp.tag.service.mapper;

import microapp.tag.domain.ParentTypeTag;
import microapp.tag.service.dto.ParentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParentTypeTag} and its DTO {@link ParentTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParentTypeMapper extends EntityMapper<ParentTypeDTO, ParentTypeTag> {}
