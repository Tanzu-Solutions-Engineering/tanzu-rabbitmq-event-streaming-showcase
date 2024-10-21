package showcase.scdf.jdbc.processor.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * Insert new data or update existing data
 *
 * @author gregory green
 */
@Slf4j
@Component
public class SqlQueryConsumer implements Function<String,String> {
    private final String sql;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SqlQueryConsumer(NamedParameterJdbcTemplate jdbcTemplate, @Value("${jdbc.sql.query}")String sql) {
        this.sql = sql;
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

        var out = objectMapper.writeValueAsString(outMap);
        log.info("Returning: {}",out);
        return out;
    }
}
