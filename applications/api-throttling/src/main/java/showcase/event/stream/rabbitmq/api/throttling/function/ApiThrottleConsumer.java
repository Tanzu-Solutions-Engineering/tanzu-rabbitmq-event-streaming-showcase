package showcase.event.stream.rabbitmq.api.throttling.function;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.exception.CommunicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.function.Consumer;


/**
 *
 */
@Component
@Slf4j
public class ApiThrottleConsumer implements Consumer<Message<String>> {

    private final RestTemplate restTemplate;
    private final URI uri;

    @SneakyThrows
    public ApiThrottleConsumer(RestTemplate restTemplate, @Value("${api.throttling.url}") String uri) {
        this.restTemplate = restTemplate;
        this.uri = new URI(uri);
    }

    @Override
    public void accept(Message<String> message) {
        log.info("Message: {}", message);

        var requestHttp = new HttpHeaders();
        requestHttp.setContentType(MediaType.APPLICATION_JSON);

        var requestEntity = new HttpEntity<String>(message.getPayload(), requestHttp);
        var response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().isError())
            throw new CommunicationException(response.toString());
    }
}
