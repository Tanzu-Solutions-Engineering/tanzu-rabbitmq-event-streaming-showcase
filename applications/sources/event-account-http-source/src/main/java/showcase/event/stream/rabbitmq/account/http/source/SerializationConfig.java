package showcase.event.stream.rabbitmq.account.http.source;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializationConfig {
    @Bean
    Converter<Account,byte[]> converter(ObjectMapper objectMapper)
    {
        return account -> {
            try {
                return objectMapper.writeValueAsBytes(account);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
