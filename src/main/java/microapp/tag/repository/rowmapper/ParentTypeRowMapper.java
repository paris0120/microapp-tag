package microapp.tag.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import microapp.tag.domain.ParentTypeTag;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ParentTypeTag}, with proper type conversions.
 */
@Service
public class ParentTypeRowMapper implements BiFunction<Row, String, ParentTypeTag> {

    private final ColumnConverter converter;

    public ParentTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ParentTypeTag} stored in the database.
     */
    @Override
    public ParentTypeTag apply(Row row, String prefix) {
        ParentTypeTag entity = new ParentTypeTag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTopic(converter.fromRow(row, prefix + "_topic", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        entity.setParentType(converter.fromRow(row, prefix + "_parent_type", String.class));
        entity.setServer(converter.fromRow(row, prefix + "_server", String.class));
        entity.setUserManageable(converter.fromRow(row, prefix + "_user_manageable", Boolean.class));
        entity.setIsEncrypted(converter.fromRow(row, prefix + "_is_encrypted", Boolean.class));
        return entity;
    }
}
