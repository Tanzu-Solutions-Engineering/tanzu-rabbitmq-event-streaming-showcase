package showcase.scdf.csv.json.processor.function;

import nyla.solutions.core.io.csv.CsvReader;
import org.springframework.stereotype.Component;
import showcase.scdf.csv.json.processor.properties.CsvToJsonProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * Convert provided CSV to JSOn.
 *
 * Example Input
 *
 * <pre>
 *     private String[] headers = {"firstName","lastName"};
 *     var subject = new CsvToJsonFunction(new CsvToJsonProperties(headers));
 * </pre>
 *
 * Example input
 * <pre>
 * "John","Doe"
 * </pre>
 *
 * Output
 * <pre>
 *     {    "firstName": "John",
 *          "lastName" : "Doe"
 *     }
 * </pre>
 *
 *
 * @author gregory green
 */
@Component
public class CsvToJsonFunction implements Function<String, Map> {
    private final String[] headers;

    public CsvToJsonFunction(CsvToJsonProperties csvToJsonProperties ) {
        this.headers = csvToJsonProperties.getHeaders();
    }

    @Override
    public Map<String,String> apply(String csv) {
        if(csv == null || csv.isEmpty())
            return Collections.emptyMap();

        var cells = CsvReader.parse(csv);
        Map<String,String> map = new HashMap<>();
        for (int i=0;i < headers.length;i++)
        {
            var value = i < cells.size() ? cells.get(i) : "";
            map.put(headers[i],value);
        }
        return map;
    }
}
