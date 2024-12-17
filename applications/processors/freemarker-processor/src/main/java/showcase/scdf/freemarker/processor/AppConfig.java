package showcase.scdf.freemarker.processor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.scdf.freemarker.processor.properties.FreemarkerProperties;

@Configuration
@EnableConfigurationProperties(FreemarkerProperties.class)
public class AppConfig {
}
