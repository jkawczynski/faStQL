package pl.jkawczynski.fastql.dao;

import java.util.Date;
import pl.jkawczynski.fastql.annotations.SqlColumn;
import pl.jkawczynski.fastql.annotations.SqlId;
import pl.jkawczynski.fastql.annotations.SqlTable;

/**
 *
 * @author jkawczynski
 */
@SqlTable(name = "test_ent")
class TestJUnitEntity {

    @SqlId
    @SqlColumn(name = "identi_")
    private Long id;

    @SqlColumn(name = "some_date")
    private Date someDate;

    @SqlColumn(name = "some_string")
    private String someString;

    private String nonSqlField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSomeDate() {
        return someDate;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public String getNonSqlField() {
        return nonSqlField;
    }

    public void setNonSqlField(String nonSqlField) {
        this.nonSqlField = nonSqlField;
    }

}
