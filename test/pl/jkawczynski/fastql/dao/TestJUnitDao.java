package pl.jkawczynski.fastql.dao;

import java.sql.Connection;
import java.sql.SQLException;
import pl.jkawczynski.fastql.core.AbstractJdbcDao;

/**
 *
 * @author jkawczynski
 */
class TestJUnitDao extends AbstractJdbcDao<TestJUnitEntity> {

    @Override
    protected Connection getConnection() throws SQLException {
        return null;
    }

}
