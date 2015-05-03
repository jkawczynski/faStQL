package pl.jkawczynski.fastql.mappers;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.jkawczynski.fastql.annotations.SqlColumn;

/**
 *
 * @author jkawczynski
 */
public class SimpleEntityMapper {

    public static <T> T map(ResultSet rs, Class<?> clazz) {
        T entity = null;
        try {
            Field[] fields = clazz.getDeclaredFields();
            while (rs.next()) {
                entity = (T) clazz.newInstance();
                for (Field field : fields) {
                    SqlColumn annotation = field.getAnnotation(SqlColumn.class);
                    if (annotation != null) {
                        field.setAccessible(true);
                        field.set(entity, rs.getObject(annotation.name()));
                    }
                }
            }
            return entity;
        } catch (InstantiationException ex) {
            Logger.getLogger(SimpleEntityMapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SimpleEntityMapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SimpleEntityMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity;

    }

}
