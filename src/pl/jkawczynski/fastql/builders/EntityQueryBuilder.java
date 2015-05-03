package pl.jkawczynski.fastql.builders;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author jkawczynski
 */
public class EntityQueryBuilder {

    public class Expressions {

        public static final String SELECT = " SELECT ";
        public static final String FROM = " FROM ";
        public static final String WHERE = " WHERE ";
        public static final String CREATE = " CREATE ";
        public static final String UPDATE = " UPDATE ";
        public static final String SET = " SET ";
        public static final String INSERT_INTO = " INSERT INTO ";
        public static final String DELETE_FROM = " DELETE FROM ";
    }

    public static String buildFindByIdSql(Collection columnNames, String tableName, String idColumnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildFindAllSql(columnNames, tableName));
        sb.append(EntityQueryBuilder.Expressions.WHERE);
        sb.append(idColumnName);
        sb.append(" = ?");
        return sb.toString();
    }

    public static String buildFindAllSql(Collection columnNames, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(EntityQueryBuilder.Expressions.SELECT);
        for (Iterator iterator = columnNames.iterator(); iterator.hasNext();) {
            appendNext(sb, iterator);
        }
        sb.append(EntityQueryBuilder.Expressions.FROM);
        sb.append(tableName);
        return sb.toString();
    }

    public static String buildUpdateSql(Collection<String> columnNames, String tableName, String idColumnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(EntityQueryBuilder.Expressions.UPDATE).append(tableName).append(EntityQueryBuilder.Expressions.SET);
        for (Iterator iterator = columnNames.iterator(); iterator.hasNext();) {
            String columnName = (String) iterator.next();
            if (!columnName.equals(idColumnName)) {
                sb.append(columnName);
                if (iterator.hasNext()) {
                    sb.append(" = ?, ");
                } else {
                    sb.append(" = ?");
                }
            }
        }
        sb.append(EntityQueryBuilder.Expressions.WHERE).append(idColumnName).append(" = ?");
        return sb.toString();
    }

    public static String buildInsertSql(Collection<String> columnNames, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(EntityQueryBuilder.Expressions.INSERT_INTO).append(tableName).append(" ( ");
        for (Iterator iterator = columnNames.iterator(); iterator.hasNext();) {
            appendNext(sb, iterator);
        }
        sb.append(" ) VALUES ( ");
        for (int i = 0; i < columnNames.size(); i++) {
            sb.append("?");
            if ((i + 1) < columnNames.size()) {
                sb.append(", ");
            }
        }
        sb.append(" )");
        return sb.toString();

    }

    private static void appendNext(StringBuilder sb, Iterator iterator) {
        sb.append((String) iterator.next());
        if (iterator.hasNext()) {
            sb.append(", ");
        }
    }

    public static String buildDeleteSql(String idColumnName, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(EntityQueryBuilder.Expressions.DELETE_FROM).append(tableName).append(EntityQueryBuilder.Expressions.WHERE).append(idColumnName).append(" = ?");
        return sb.toString();
    }

}
