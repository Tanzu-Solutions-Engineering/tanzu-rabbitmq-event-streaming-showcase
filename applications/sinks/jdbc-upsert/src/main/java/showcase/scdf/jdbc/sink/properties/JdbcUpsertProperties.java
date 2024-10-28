package showcase.scdf.jdbc.sink.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jdbc.upsert")
public class JdbcUpsertProperties {
    /**
     * SQL update
     */
    private String updateSql;

    /**
     * SQL insert
     */
    private String insertSql;

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }
}
