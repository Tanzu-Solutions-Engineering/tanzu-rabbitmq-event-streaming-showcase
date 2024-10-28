package showcase.scdf.jdbc.processor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.scdf.jdbc.processor.properties.JdbcSqlProperties;

@Configuration
@EnableConfigurationProperties(JdbcSqlProperties.class)
public class AppConfig {
}
