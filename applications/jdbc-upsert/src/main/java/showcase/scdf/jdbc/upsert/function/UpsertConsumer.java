package showcase.scdf.jdbc.upsert.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Insert new data or update existing data
 *
 * @author gregory green
 */
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

     @SneakyThrows
     Map<String, Object> upsert(Map<String, Object> dataRow ) {

        var inputMap = new HashMap<>(dataRow);

         inputMap.put("payload",objectMapper.writeValueAsString(dataRow));
        var cnt = jdbcTemplate.update(updateSql, inputMap);
        log.info("Update count: {} for update SQL: {}",cnt,updateSql);

        if (cnt == 0) {
            log.info("Inserting with SQL: {}",insertSql);

            jdbcTemplate.update(insertSql, inputMap);
        }

        return inputMap;
    }
}
