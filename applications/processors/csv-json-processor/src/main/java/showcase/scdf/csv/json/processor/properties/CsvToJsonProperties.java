package showcase.scdf.csv.json.processor.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gregory green
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "csv.to.json")
public class CsvToJsonProperties {

    /**
     * CSV Headers by position to convert to JSON.
     *
     * ex: csv.to.json.headers=firstName,lastMName
     *
     * Input: joe,doe
     * Output {"firstName" : "joe"
     *         , "lastMName" : "doe"}
     *
     */
    private String[]  headers;
}


