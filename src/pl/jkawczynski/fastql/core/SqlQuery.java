package pl.jkawczynski.fastql.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jkawczynski
 */
public class SqlQuery {

    private String sqlQuery;
    private Map<Integer, Object> parameters = new HashMap<Integer, Object>();

    public SqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public SqlQuery addParameter(Integer paramIndex, Object value) {
        parameters.put(paramIndex, value);
        return this;
    }

    public void getParameter(Integer paramIndex) {
        parameters.get(paramIndex);
    }

    public Map<Integer, Object> getParameters() {
        return parameters;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

}
