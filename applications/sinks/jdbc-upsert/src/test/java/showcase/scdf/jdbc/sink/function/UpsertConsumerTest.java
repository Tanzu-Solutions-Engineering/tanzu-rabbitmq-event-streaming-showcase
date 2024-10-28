package showcase.scdf.jdbc.sink.function;

import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import showcase.scdf.jdbc.sink.properties.JdbcUpsertProperties;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpsertConsumerTest {

    @Mock
     private NamedParameterJdbcTemplate jdbcTemplate;
    private String updateSql = "update";
    private UpsertConsumer subject;
    private Map<String,Object> dataRow;


    @BeforeEach
    void setUp() {
        subject = new UpsertConsumer(jdbcTemplate, JdbcUpsertProperties.builder().updateSql(updateSql).insertSql("").build());
        dataRow = Map.of("col1", "dsds" );
    }

    @Test
    void acceptJsonDataRow_empty() {
        var subject = new UpsertConsumer(jdbcTemplate, new JdbcUpsertProperties());

        var emptyObject = "{}";

        when(jdbcTemplate.update(anyString(),any(Map.class))).thenReturn( 1);

        subject.accept(emptyObject);
        verify(jdbcTemplate).update(anyString(),
                any(Map.class));
    }

    @Test
    void acceptJsonDataRow_invalid() {
        var subject = new UpsertConsumer(jdbcTemplate, new JdbcUpsertProperties());

        var invalid = "{";
        assertThrows(JsonParseException.class, () ->
            subject.accept(invalid));
    }

    @Test
    void test_update() {

        Map<String,Object> dataRow = Map.of("col1" ,"dsds");

        var updateSql = "insert into table values(?,?)";

        var subject = new UpsertConsumer(jdbcTemplate, JdbcUpsertProperties.builder().updateSql(updateSql).build());
        when(jdbcTemplate.update(anyString(),any(Map.class))).thenReturn( 1);

        subject.upsert(dataRow);

        verify(jdbcTemplate).update(anyString(),
                any(Map.class));
    }

    @Test
    void test_insert() {

        Map<String,Object> dataRow = Map.of("col1", "dsds" );

        var insertSql = "insert into table values(:col1)";
        var updateSql = "update table set values cl=:col1";

        var subject = new UpsertConsumer(jdbcTemplate, JdbcUpsertProperties.builder()
                .updateSql(updateSql).insertSql(insertSql).build());

        when(jdbcTemplate.update(anyString(),any(Map.class))).thenReturn( 0)
                .thenReturn(1);


        subject.upsert(dataRow);

        verify(jdbcTemplate,times(2))
                .update(anyString(), any(Map.class));

    }

    @Test
    void addPayload() {

        var actual = subject.upsert(dataRow);

        System.out.println(actual);

        assertThat(actual.get("payload")).isNotNull();
    }
}