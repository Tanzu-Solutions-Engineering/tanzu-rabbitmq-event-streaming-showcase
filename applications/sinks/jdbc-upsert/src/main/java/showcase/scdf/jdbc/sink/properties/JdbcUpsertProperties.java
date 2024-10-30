package showcase.scdf.jdbc.sink.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(prefix = "jdbc.upsert")
public class JdbcUpsertProperties {
    /**
     * SQL update statement jdbc.upsert.updateSql. Ex: update dei_users.members set MEMBER_NM = :name where MEMBER_ID =:id
     *
     */
    private String updateSql = "";

    /**
     * SQL insert statement jdbc.upsert.insertSql  Ex:insert into dei_users.members(MEMBER_ID,MEMBER_NM) values(:id,:name)
     */
    private String insertSql = "";

}
