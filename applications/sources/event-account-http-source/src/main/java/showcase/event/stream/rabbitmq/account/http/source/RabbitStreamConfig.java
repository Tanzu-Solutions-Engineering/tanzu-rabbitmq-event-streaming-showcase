package showcase.event.stream.rabbitmq.account.http.source;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.integration.Publisher;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

import static java.lang.String.valueOf;

@Configuration
@Slf4j
@Profile("stream")
public class RabbitStreamConfig {

    private static final String FILTER_PROP_NM = "stateProvince";

    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String streamName;

    @Value("${rabbitmq.streaming.use.filter:false}")
    private boolean isUseFilter;

    @Bean
    Queue stream(Environment environment) {
        log.info("Creating stream: {}",streamName);

        environment.streamCreator().name(streamName)
                .create();

        return QueueBuilder.durable(streamName)
                .build();
    }

    @Bean
    Producer producer(Environment environment)
    {
        log.info("stream: {}, isUseFilter: {}",streamName,isUseFilter);
        var builder = environment.producerBuilder()
                .stream(streamName);

        if(isUseFilter){
               builder = builder.filterValue(msg ->
                    valueOf(msg.getApplicationProperties().get(FILTER_PROP_NM)));
        }

        return builder.build();
    }

    @Bean
    Publisher<Account> publisher(Producer producer, Converter<Account,byte[]> converter)
    {
        return account ->{

            var state = account.getLocation() != null ? account.getLocation().getStateProvince() : "";

            producer.send(producer.messageBuilder()
                    .addData(converter.convert(account))
                    .properties().messageId(account.getId())
                    .messageBuilder()
                    .applicationProperties().entry(FILTER_PROP_NM,state)
                    .messageBuilder()
                    .build(), confirmationStatus ->{});
        };
    }

    @Bean
    RabbitStreamTemplate rabbitStreamTemplate(Environment environment) {
        return new RabbitStreamTemplate(environment,streamName);
    }

}
