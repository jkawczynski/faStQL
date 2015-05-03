package pl.jkawczynski.fastql.inrefaces;

import java.sql.PreparedStatement;

/**
 *
 * @author jkawczynski
 */
public interface JdbcCallback {

    Object doInJdbc(PreparedStatement pstmt) throws Exception;
}
