package microapp.tag.service.mapper;

import microapp.tag.domain.TagTag;
import microapp.tag.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagTag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, TagTag> {}
