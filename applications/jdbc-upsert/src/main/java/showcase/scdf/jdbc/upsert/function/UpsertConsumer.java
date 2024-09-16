package showcase.scdf.jdbc.upsert.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class UpsertConsumer implements Consumer<String> {
    private final String updateSql;
    private final String insertSql;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public UpsertConsumer(NamedParameterJdbcTemplate jdbcTemplate, @Value("${app.updateSql}")String updateSql, @Value("${app.insertSql}") String insertSql) {
        this.updateSql = updateSql;
        this.insertSql = insertSql;
        this.jdbcTemplate = jdbcTemplate;
    }


    @lombok.SneakyThrows
    @Override
    public void accept(String json) {
        log.info("JSON: {}",json);
        var dataRow  = objectMapper.readValue(json, Map.class);
        upsert(dataRow);
    }

     void upsert(Map<String, Object> dataRow ) {

        var cnt = jdbcTemplate.update(updateSql, dataRow);
        log.info("Update count: {} for update SQL: {}",cnt,updateSql);

        if (cnt == 0) {
            log.info("Inserting with SQL: {}",insertSql);

            jdbcTemplate.update(insertSql, dataRow);
        }
    }
}
