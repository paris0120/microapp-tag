package microapp.tag.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ServerSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("server", table, columnPrefix + "_server"));
        columns.add(Column.aliased("uuid", table, columnPrefix + "_uuid"));
        columns.add(Column.aliased("decoder", table, columnPrefix + "_decoder"));
        columns.add(Column.aliased("password", table, columnPrefix + "_password"));

        return columns;
    }
}
