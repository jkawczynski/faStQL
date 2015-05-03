package pl.jkawczynski.fastql.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jkawczynski
 */
public class TransactionManager extends AbstractTransactionManager {

    public TransactionManager(Connection connection) {
        super(connection);
    }

    @Override
    public AbstractTransactionManager beginTransaction() {
        try {
            connection.setAutoCommit(false);
            inTransaction = true;
        } catch (SQLException ex) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    @Override
    public AbstractTransactionManager addTransaction(SqlQuery sqlQuery) {
        if (!inTransaction) {
            throw new TransactionNotReadyException();
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sqlQuery.getSqlQuery());
            for (Entry<Integer, Object> entry : sqlQuery.getParameters().entrySet()) {
                pstmt.setObject(entry.getKey(), entry.getValue());
            }
            pstmts.add(pstmt);
        } catch (SQLException ex) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this;
    }

    @Override
    public AbstractTransactionManager addTransaction(String sqlQuery) {
        if (!inTransaction) {
            throw new TransactionNotReadyException();
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
            pstmts.add(pstmt);
        } catch (SQLException ex) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    @Override
    public void executeAndCommit() {
        if (!inTransaction) {
            throw new TransactionNotReadyException();
        }
        try {
            for (PreparedStatement pstmt : pstmts) {
                pstmt.executeUpdate();
            }
            connection.commit();
            inTransaction = false;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
