package showcase.scdf.jdbc.processor.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "jdbc.sql")
public class JdbcSqlProperties {
    /**
     * JDBC sql query
     *
     * ex: select id, payload->> 'lossType' as lossType,
     * payload-> 'insured' ->> 'name' as name,
     * concat( payload->'insured'->'homeAddress' ->> 'street', ', ', payload->'insured'->'homeAddress' ->> 'city', ', ', payload ->'insured'->'homeAddress' ->> 'state', ' ', payload -> 'insured'->'homeAddress' ->> 'zip') as homeAddress
     * from insurance.claims WHERE id= :id
     */
    @NotBlank
    private String query;
}


