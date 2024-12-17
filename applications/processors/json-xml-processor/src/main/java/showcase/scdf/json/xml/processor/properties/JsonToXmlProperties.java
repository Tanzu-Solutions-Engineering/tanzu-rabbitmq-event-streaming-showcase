package showcase.scdf.json.xml.processor.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "json.to.xml")
public class JsonToXmlProperties {
    /**
     * The XML root name used in the XML output.
     */
    private String rootName;
}
