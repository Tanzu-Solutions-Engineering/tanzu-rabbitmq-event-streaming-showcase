package showcase.scdf.csv.json.processor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.scdf.csv.json.processor.properties.CsvToJsonProperties;

@Configuration
@EnableConfigurationProperties(CsvToJsonProperties.class)
public class AppConfig {
}
