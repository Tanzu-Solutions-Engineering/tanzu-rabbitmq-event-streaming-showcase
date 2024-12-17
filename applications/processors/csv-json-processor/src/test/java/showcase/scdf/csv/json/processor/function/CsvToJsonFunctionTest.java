package showcase.scdf.csv.json.processor.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import showcase.scdf.csv.json.processor.properties.CsvToJsonProperties;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvToJsonFunctionTest {

    private CsvToJsonFunction subject;
    private String csv = """
            "John","Doe"
            """;

    private String multiLineCsv = """
            "Jill","Doe"
            "John","Doe"
            """;

    private String[] headers = {"firstName","lastName"};

    @BeforeEach
    void setUp() {
        subject = new CsvToJsonFunction(new CsvToJsonProperties(headers));
    }

    @Test
    void applyEmpty() {
        var expected = Map.of();

        assertEquals(expected, subject.apply(""));
    }

    @Test
    void applyNull() {
        var expected = Map.of();

        assertEquals(expected, subject.apply(null));
    }

    @Test
    void apply() {
        var expected = Map.of(
                "firstName","John",
                "lastName", "Doe");

        assertEquals(expected, subject.apply(csv));

    }

    @Test
    void apply_multipleLine_firstOnly() {
        var expected = Map.of(
                "firstName","Jill",
                "lastName", "Doe");

        assertEquals(expected, subject.apply(multiLineCsv));

    }

    @Test
    void apply_leastValueThanHeader() {
        var expected = Map.of(
                "firstName","Hello",
                "lastName", "");

        String csvText = """
                Hello""";
        assertEquals(expected, subject.apply(csvText));

    }
}