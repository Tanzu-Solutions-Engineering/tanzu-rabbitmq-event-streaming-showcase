package showcase.scdf.freemarker.processor.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.scdf.freemarker.processor.properties.FreemarkerProperties;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreemarkerFunctionTest {

    private Map<String, Object> map;

    @Mock
    private FreemarkerProperties properties;
    private String expectedKey = "key";
    private Object expectedValue  = "value";
    private FreemarkerFunction subject;
    private String template = """
            Hello ${key} 
            """;

    @BeforeEach
    void setUp() {
        when(properties.getTemplate()).thenReturn(template);
        subject = new FreemarkerFunction(properties);
    }

    @Test
    void apply() {



        map = Map.of(expectedKey,expectedValue);

        var actual = subject.apply(map);

        assertThat(actual).isNotNull();
        assertThat(actual.getPayload()).contains(expectedValue.toString());

    }
}