package showcase.scdf.jdbc.processor.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import showcase.scdf.jdbc.processor.properties.JdbcSqlProperties;

import java.util.Map;
import java.util.function.Function;

/**
 * Performance SQL query most on provided payload.
 *
 * Note if the select contains a single column with key "payload",
 * then only that is returns as a text.
 *
 * @author gregory green
 */
@Slf4j
@Component
public class SqlQueryProcessor implements Function<String,String> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String sql;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final static String PAYLOAD_KEY_NM = "payload";

    public SqlQueryProcessor(NamedParameterJdbcTemplate jdbcTemplate, JdbcSqlProperties jdbcSqlProperties) {
        this.sql = jdbcSqlProperties.getQuery();
        this.jdbcTemplate = jdbcTemplate;
    }


    @SneakyThrows
    @Override
    public String apply(String json) {
        log.info("JSON: {}",json);
        var inputMap  = objectMapper.readValue(json, Map.class);
        inputMap.put("payload",json);

        var outMap = jdbcTemplate.queryForMap(sql, inputMap);
        log.info("SQL: {}, results: {}",sql,outMap);

        if(outMap.size() == 1 && outMap.containsKey(PAYLOAD_KEY_NM)) {
            var payload = String.valueOf(outMap.get(PAYLOAD_KEY_NM));
            log.info("Returning payload: {}",payload);
            return payload;
        }

        var out = objectMapper.writeValueAsString(outMap);
        log.info("Returning: {}",out);
        return out;
    }
}
