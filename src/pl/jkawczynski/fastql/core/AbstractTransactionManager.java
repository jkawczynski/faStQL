package pl.jkawczynski.fastql.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jkawczynski
 */
public abstract class AbstractTransactionManager {

    protected boolean inTransaction = false;
    protected Connection connection = null;
    protected List<PreparedStatement> pstmts = new ArrayList<PreparedStatement>();

    public AbstractTransactionManager(Connection connection) {
        this.connection = connection;
    }

    public abstract AbstractTransactionManager beginTransaction();

    public abstract AbstractTransactionManager addTransaction(SqlQuery pstmt);

    public abstract AbstractTransactionManager addTransaction(String sqlQuery);

    public abstract void executeAndCommit();

}
