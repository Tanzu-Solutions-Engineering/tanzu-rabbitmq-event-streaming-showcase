package showcase.event.stream.rabbitmq.api.throttling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {
    @Bean
    RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
