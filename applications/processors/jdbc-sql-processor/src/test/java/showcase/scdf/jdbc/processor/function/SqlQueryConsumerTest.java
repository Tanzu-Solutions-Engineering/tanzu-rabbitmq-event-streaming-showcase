package showcase.scdf.jdbc.processor.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqlQueryConsumerTest {

    @Mock
     private NamedParameterJdbcTemplate jdbcTemplate;
    private String sql = "select col1,col2 from table where col3 = :id";
    private SqlQueryConsumer subject;
    private String json;
    private Map expectedMap;
    private String expectedJson;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        subject = new SqlQueryConsumer(jdbcTemplate, sql);
        json = """
                {
                    "id": "1232",
                    "name": "1232"
                }
                """;
    }


    @Test
    void apply() throws JsonProcessingException {

        expectedMap = Map.of("id", "1232", "name", "1232");


        var subject = new SqlQueryConsumer(jdbcTemplate, sql);
        when(jdbcTemplate.queryForMap(anyString(),any(Map.class))).thenReturn( expectedMap);

        var actual = objectMapper.readValue(subject.apply(json),Map.class);

        assertThat(actual).isEqualTo(expectedMap);
    }

}