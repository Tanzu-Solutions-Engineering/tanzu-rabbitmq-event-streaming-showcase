package showcase.scdf.freemarker.processor.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "freemarker")
public class FreemarkerProperties {
    /**
     * Media Types (Ex: application/json)
     */
    private String contentType;

    /**
     * Freemarker Template Example
     *
     * See https://freemarker.apache.org/docs/dgui_template_overallstructure.html
     *
     * ${firstName} ${lastName}
     */
    private String template;
}
