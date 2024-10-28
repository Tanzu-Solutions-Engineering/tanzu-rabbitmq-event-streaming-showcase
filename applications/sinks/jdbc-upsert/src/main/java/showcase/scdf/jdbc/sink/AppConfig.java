package showcase.scdf.jdbc.sink;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import showcase.scdf.jdbc.sink.properties.JdbcUpsertProperties;

@Configuration
@EnableConfigurationProperties(JdbcUpsertProperties.class)
public class AppConfig {
}
