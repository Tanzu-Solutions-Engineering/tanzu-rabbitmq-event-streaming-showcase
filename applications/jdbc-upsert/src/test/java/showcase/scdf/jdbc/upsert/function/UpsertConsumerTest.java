package showcase.scdf.jdbc.upsert.function;

import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpsertConsumerTest {

    @Mock
     private NamedParameterJdbcTemplate jdbcTemplate;


    @BeforeEach
    void setUp() {

    }

    @Test
    void acceptJsonDataRow_empty() {
        var subject = new UpsertConsumer(jdbcTemplate, "", "");

        var emptyObject = "{}";

        when(jdbcTemplate.update(anyString(),any(Map.class))).thenReturn( 1);

        subject.accept(emptyObject);
        verify(jdbcTemplate).update(anyString(),
                any(Map.class));
    }

    @Test
    void acceptJsonDataRow_invalid() {
        var subject = new UpsertConsumer(jdbcTemplate, "", "");

        var invalid = "{";
        assertThrows(JsonParseException.class, () ->
            subject.accept(invalid));
    }

    @Test
    void test_update() {

        Map<String,Object> dataRow = Map.of("col1" ,"dsds");

        var updateSql = "insert into table values(?,?)";

        var subject = new UpsertConsumer(jdbcTemplate, updateSql, "");
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

        var subject = new UpsertConsumer(jdbcTemplate, updateSql, insertSql);

        when(jdbcTemplate.update(updateSql,dataRow)).thenReturn( 0);
        when(jdbcTemplate.update(insertSql,dataRow)).thenReturn(1);


        subject.upsert(dataRow);

        verify(jdbcTemplate,times(1))
                .update(insertSql, dataRow);

    }
}