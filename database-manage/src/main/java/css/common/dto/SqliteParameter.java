package css.common.dto;

/**
 * @author Ng_Chun-fai
 * @date 2022年01月18日 9:06
 */
public class SqliteParameter {
    private String value;

    private String type;

    public SqliteParameter() {
    }

    public SqliteParameter(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
