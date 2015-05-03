package pl.jkawczynski.fastql.core;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pl.jkawczynski.fastql.inrefaces.JdbcCallback;
import pl.jkawczynski.fastql.annotations.SqlColumn;
import pl.jkawczynski.fastql.annotations.SqlId;
import pl.jkawczynski.fastql.builders.EntityQueryBuilder;
import pl.jkawczynski.fastql.annotations.SqlTable;
import pl.jkawczynski.fastql.mappers.SimpleEntityMapper;

/**
 *
 * @author jkawczynski
 */
public abstract class AbstractJdbcDao<T> extends JdbcSessionManager {

    private final Class<T> entityClass;
    private final Map<String, String> fieldNameToColumnNameMap = new LinkedHashMap<String, String>();
    private final List<Field> annotatedEntityFields = new ArrayList<Field>();
    private Field idField;

    protected AbstractJdbcDao() {
        Type type = getClass().getGenericSuperclass();
        while (!(type instanceof ParameterizedType) || ((ParameterizedType) type).getRawType() != AbstractJdbcDao.class) {
            if (type instanceof ParameterizedType) {
                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
            } else {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }
        this.entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        initFieldsArray();
        initColumnToFieldMap();
    }

    public T findById(final Long id) throws SQLException, Exception {
        return (T) doInSession(new JdbcCallback() {
            @Override
            public Object doInJdbc(PreparedStatement pstmt) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                T entityObj = null;
                while (rs.next()) {
                    entityObj = SimpleEntityMapper.map(rs, entityClass);
                }
                return entityObj;
            }
        }, getFindByIdQuery());
    }

    public List<T> findAll() throws SQLException, Exception {
        return (List<T>) doInSession(new JdbcCallback() {
            @Override
            public Object doInJdbc(PreparedStatement pstmt) throws Exception {
                List<T> entities = new ArrayList<T>();
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    T entity = SimpleEntityMapper.map(rs, entityClass);
                    if (entity != null) {
                        entities.add(entity);
                    }
                }
                return entities;
            }
        }, getFindAllQuery());

    }

    public void update(T entity) throws Exception {
        doInSession(new JdbcCallback() {
            @Override
            public Object doInJdbc(PreparedStatement pstmt) throws Exception {
                for (int i = 0; i <= annotatedEntityFields.size(); i++) {
                    if (i == annotatedEntityFields.size()) {
                        pstmt.setObject(i + 1, idField.get(entity));
                        break;
                    } else {
                        pstmt.setObject(i + 1, annotatedEntityFields.get(i).get(entity));
                    }
                }

                pstmt.executeUpdate();
                return null;
            }
        }, getUpdateQuery());
    }

    public void save(T entity) throws SQLException, Exception {
        doInSession(new JdbcCallback() {
            @Override
            public Object doInJdbc(PreparedStatement pstmt) throws Exception {
                for (int i = 0; i < annotatedEntityFields.size(); i++) {
                    annotatedEntityFields.get(i).setAccessible(true);
                    Object value = annotatedEntityFields.get(i).get(entity);
                    if (value instanceof Date) {
                        java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
                        pstmt.setDate(i + 1, sqlDate);
                    } else {
                        pstmt.setObject(i + 1, value);
                    }

                }
                pstmt.executeUpdate();
                return null;
            }
        }, getInsertQuery());
    }

    public void delete(T entity) throws SQLException, Exception {
        doInSession(new JdbcCallback() {
            @Override
            public Object doInJdbc(PreparedStatement pstmt) throws Exception {
                pstmt.setObject(1, (Long) idField.get(entity));
                pstmt.executeUpdate();
                return null;
            }
        }, getDelteQuery());
    }

    protected String getColumnNameByFieldName(String name) {
        return fieldNameToColumnNameMap.get(name);
    }

    protected String getTableName() {
        return entityClass.getAnnotation(SqlTable.class).name();
    }

    protected String getIdColumnName() {
        String name = null;
        for (Field field : annotatedEntityFields) {
            if (field.getAnnotation(SqlId.class) != null) {
                name = field.getAnnotation(SqlColumn.class).name();
            }
        }
        return name;

    }

    private void initColumnToFieldMap() {
        for (Field field : annotatedEntityFields) {
            SqlColumn annotation = field.getAnnotation(SqlColumn.class);
            if (annotation != null) {
                fieldNameToColumnNameMap.put(field.getName(), annotation.name());
            }
        }
    }

    private void initFieldsArray() {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(SqlColumn.class) != null) {
                field.setAccessible(true);
                annotatedEntityFields.add(field);
                if (field.getAnnotation(SqlId.class) != null) {
                    idField = field;
                }
            }
        }
    }

    private String getFindByIdQuery() {
        return EntityQueryBuilder.buildFindByIdSql(fieldNameToColumnNameMap.values(), getTableName(), getIdColumnName());
    }

    private String getFindAllQuery() {
        return EntityQueryBuilder.buildFindAllSql(fieldNameToColumnNameMap.values(), getTableName());
    }

    private String getUpdateQuery() {
        return EntityQueryBuilder.buildUpdateSql(fieldNameToColumnNameMap.values(), getTableName(), getIdColumnName());
    }

    private String getInsertQuery() {
        return EntityQueryBuilder.buildInsertSql(fieldNameToColumnNameMap.values(), getTableName());
    }

    private String getDelteQuery() {
        return EntityQueryBuilder.buildDeleteSql(getIdColumnName(), getTableName());
    }

}
