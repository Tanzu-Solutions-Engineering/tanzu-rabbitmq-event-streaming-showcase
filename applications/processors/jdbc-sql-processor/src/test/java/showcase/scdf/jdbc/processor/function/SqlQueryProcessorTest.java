package showcase.scdf.jdbc.processor.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import showcase.scdf.jdbc.processor.properties.JdbcSqlProperties;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqlQueryProcessorTest {

    @Mock
     private NamedParameterJdbcTemplate jdbcTemplate;
    private String sql = "select col1,col2 from table where col3 = :id";
    private SqlQueryProcessor subject;
    private String json;
    private Map expectedMap;
    private String expectedJson;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JdbcSqlProperties sqlProperties = new JdbcSqlProperties(sql);


    @BeforeEach
    void setUp() {
        subject = new SqlQueryProcessor(jdbcTemplate, sqlProperties);
        json = """
                {
                    "id": "1232",
                    "name": "1232"
                }
                """;
    }


    @DisplayName("Given Result with columns When apply Then return json of results")
    @Test
    void apply() throws JsonProcessingException {

        expectedMap = Map.of("id", "1232", "name", "1232");


        var subject = new SqlQueryProcessor(jdbcTemplate, sqlProperties);
        when(jdbcTemplate.queryForMap(anyString(),any(Map.class))).thenReturn( expectedMap);

        var actual = objectMapper.readValue(subject.apply(json),Map.class);

        assertThat(actual).isEqualTo(expectedMap);
    }

    @DisplayName("Given Result with 1 payload colum When apply Then return the text payload")
    @Test
    void singlePayloadReturnTextAsIs() {

        var expectedText = "expected";
        expectedMap = Map.of("payload", expectedText);

        var subject = new SqlQueryProcessor(jdbcTemplate, sqlProperties);
        when(jdbcTemplate.queryForMap(anyString(),any(Map.class))).thenReturn( expectedMap);

        var actual = subject.apply(json);

        assertThat(actual).isEqualTo(expectedText);

    }


    @DisplayName("Given Result with payload + other When apply Then return json of all fields")
    @Test
    void payloadWithMultiReturnsJson() throws JsonProcessingException {

        expectedMap = Map.of("id", "1232", "payload", "1232");


        var subject = new SqlQueryProcessor(jdbcTemplate, sqlProperties);
        when(jdbcTemplate.queryForMap(anyString(),any(Map.class))).thenReturn( expectedMap);

        var actual = objectMapper.readValue(subject.apply(json),Map.class);

        assertThat(actual).isEqualTo(expectedMap);

    }
}