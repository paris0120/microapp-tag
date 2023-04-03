package microapp.tag.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ParentTypeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("topic", table, columnPrefix + "_topic"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        columns.add(Column.aliased("parent_type", table, columnPrefix + "_parent_type"));
        columns.add(Column.aliased("server", table, columnPrefix + "_server"));
        columns.add(Column.aliased("user_manageable", table, columnPrefix + "_user_manageable"));
        columns.add(Column.aliased("is_encrypted", table, columnPrefix + "_is_encrypted"));

        return columns;
    }
}
