package microapp.tag.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TagSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("tag", table, columnPrefix + "_tag"));
        columns.add(Column.aliased("text_color", table, columnPrefix + "_text_color"));
        columns.add(Column.aliased("fill_color", table, columnPrefix + "_fill_color"));
        columns.add(Column.aliased("border_color", table, columnPrefix + "_border_color"));
        columns.add(Column.aliased("icon", table, columnPrefix + "_icon"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        columns.add(Column.aliased("parent_type", table, columnPrefix + "_parent_type"));
        columns.add(Column.aliased("parent_server", table, columnPrefix + "_parent_server"));
        columns.add(Column.aliased("parent_uuid", table, columnPrefix + "_parent_uuid"));

        return columns;
    }
}
