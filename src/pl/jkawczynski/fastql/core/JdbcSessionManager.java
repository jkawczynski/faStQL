package pl.jkawczynski.fastql.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.jkawczynski.fastql.inrefaces.JdbcCallback;

/**
 *
 * @author jkawczynski
 */
public abstract class JdbcSessionManager {

    protected abstract Connection getConnection() throws SQLException;

    protected Object doInSession(JdbcCallback jdbcCallback, String sql) throws SQLException, Exception {
        long start = System.currentTimeMillis();
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            System.out.println(sql);
            return jdbcCallback.doInJdbc(pstmt);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JdbcSessionManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JdbcSessionManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            long delta = System.currentTimeMillis() - start;
            System.out.println("Czas : " + delta);
        }
    }

}
