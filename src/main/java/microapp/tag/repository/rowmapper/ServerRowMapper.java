package microapp.tag.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.tag.domain.ServerTag;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ServerTag}, with proper type conversions.
 */
@Service
public class ServerRowMapper implements BiFunction<Row, String, ServerTag> {

    private final ColumnConverter converter;

    public ServerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ServerTag} stored in the database.
     */
    @Override
    public ServerTag apply(Row row, String prefix) {
        ServerTag entity = new ServerTag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setServer(converter.fromRow(row, prefix + "_server", String.class));
        entity.setUuid(converter.fromRow(row, prefix + "_uuid", UUID.class));
        entity.setDecoder(converter.fromRow(row, prefix + "_decoder", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        return entity;
    }
}
