package showcase.scdf.json.xml.processor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.scdf.json.xml.processor.properties.JsonToXmlProperties;

@Configuration
@EnableConfigurationProperties(JsonToXmlProperties.class)
public class AppConfig {

}
