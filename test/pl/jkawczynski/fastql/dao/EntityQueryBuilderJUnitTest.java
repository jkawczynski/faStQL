package pl.jkawczynski.fastql.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import pl.jkawczynski.fastql.builders.EntityQueryBuilder;

/**
 *
 * @author jkawczynski
 */
public class EntityQueryBuilderJUnitTest {

    private static final String SELECT_SQL = "select identi_, some_date, some_string from test_ent";
    private static final String UPDATE_SQL = "update test_ent set some_date = ?, some_string = ? where identi_ = ?";
    private static final String CREATE_SQL = "insert into test_ent ( identi_, some_date, some_string ) values ( ?, ?, ? )";
    private static final String REMOVE_SQL = "delete from test_ent where identi_ = ?";
    private TestJUnitDao dao;
    private Collection columnNames;
    private String tableName;
    private String idColumnName;

    @Before
    public void init() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        dao = new TestJUnitDao();
        Field field = dao.getClass().getSuperclass().getDeclaredField("fieldNameToColumnNameMap");
        field.setAccessible(true);
        columnNames = ((LinkedHashMap) field.get(dao)).values();

        Method getTableName = dao.getClass().getSuperclass().getDeclaredMethod("getTableName");
        getTableName.setAccessible(true);
        tableName = (String) getTableName.invoke(dao);

        Method getIdColumnName = dao.getClass().getSuperclass().getDeclaredMethod("getIdColumnName");
        getIdColumnName.setAccessible(true);
        idColumnName = (String) getIdColumnName.invoke(dao);

    }

    @Test
    public void select() {
        assertEquals(SELECT_SQL, EntityQueryBuilder.buildFindAllSql(columnNames, tableName).trim().toLowerCase());
    }

    @Test
    public void update() {
        assertEquals(UPDATE_SQL, EntityQueryBuilder.buildUpdateSql(columnNames, tableName, idColumnName).trim().toLowerCase());
    }

    @Test
    public void create() {
        assertEquals(CREATE_SQL, EntityQueryBuilder.buildInsertSql(columnNames, tableName).trim().toLowerCase());
    }

    @Test
    public void delete() {
        assertEquals(REMOVE_SQL, EntityQueryBuilder.buildDeleteSql(idColumnName, tableName).trim().toLowerCase());
    }

}
