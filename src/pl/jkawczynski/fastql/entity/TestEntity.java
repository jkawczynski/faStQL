package pl.jkawczynski.fastql.entity;

import java.util.Date;
import pl.jkawczynski.fastql.annotations.SqlColumn;
import pl.jkawczynski.fastql.annotations.SqlId;
import pl.jkawczynski.fastql.annotations.SqlTable;

/**
 *
 * @author jkawczynski
 */
@SqlTable(name = "test_table")
public class TestEntity {

    @SqlId
    @SqlColumn(name = "id_")
    private Long id;

    @SqlColumn(name = "jakis_string")
    private String someString;

    @SqlColumn(name = "jakas_data")
    private Date someDate;

    @SqlColumn(name = "jakis_int")
    private Integer someInteger;

    private String nonSqlField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public Date getSomeDate() {
        return someDate;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    public Integer getSomeInteger() {
        return someInteger;
    }

    public void setSomeInteger(Integer someInteger) {
        this.someInteger = someInteger;
    }

    public String getNonSqlField() {
        return nonSqlField;
    }

    public void setNonSqlField(String nonSqlField) {
        this.nonSqlField = nonSqlField;
    }

}
