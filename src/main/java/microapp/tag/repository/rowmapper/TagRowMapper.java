package microapp.tag.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.tag.domain.TagTag;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TagTag}, with proper type conversions.
 */
@Service
public class TagRowMapper implements BiFunction<Row, String, TagTag> {

    private final ColumnConverter converter;

    public TagRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TagTag} stored in the database.
     */
    @Override
    public TagTag apply(Row row, String prefix) {
        TagTag entity = new TagTag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTag(converter.fromRow(row, prefix + "_tag", String.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        entity.setParentType(converter.fromRow(row, prefix + "_parent_type", String.class));
        entity.setParentServer(converter.fromRow(row, prefix + "_parent_server", String.class));
        entity.setParentUuid(converter.fromRow(row, prefix + "_parent_uuid", UUID.class));
        return entity;
    }
}
