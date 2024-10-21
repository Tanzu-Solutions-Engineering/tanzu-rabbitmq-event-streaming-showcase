package showcase.event.stream.rabbitmq.api.throttling.function;

import nyla.solutions.core.exception.CommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiThrottleConsumerTest {

    private ApiThrottleConsumer subject;

    private Message<String> message;
    @Mock
    private RestTemplate restTemplate;
    private MessageHeaders headers  = new MessageHeaders(Map.of("http_requestMethod","POST",
            "http_requestUrl","http://localhost:7575"));

    @Mock
    private ResponseEntity responseEntity;
    private String payload = "Hello";
    private String url = "http://localhost:7575";

    /*
         result = {MessageHeaders@9307}  size = 20
     "amqp_receivedDeliveryMode" -> {MessageDeliveryMode@9332} "PERSISTENT"
     "content-length" -> {Long@9334} 292
     "http_requestMethod" -> "POST"
     "amqp_receivedExchange" -> "api-throttle"
     "amqp_deliveryTag" -> {Long@9340} 1
     "deliveryAttempt" -> {AtomicInteger@9342} "1"
     "amqp_consumerQueue" -> "api-throttle.api-throttling"
     "amqp_redelivered" -> {Boolean@9346} true
     "accept" ->
            "amqp_receivedRoutingKey" -> "api-throttle"
            "amqp_timestamp" -> {Date@9352} "Mon Jun 03 08:02:56 EDT 2024"
            "host" -> "localhost:7575"
            "amqp_messageId" -> "f351dcfc-4fb7-4ded-7ffc-49338be5d194"
            "http_requestUrl" -> "http://localhost:7575/timeout"
            "id" -> {UUID@9360} "a3b6e104-67d4-bc8f-02c1-c189c928dcd4"
            "amqp_consumerTag" -> "amq.ctag-3v0hwHqd7JB2fbET1g8WnQ"
            "sourceData" -> {Message@9364} "(Body:'[B@f3727ff(byte[292])' MessageProperties [headers={content-length=292, http_requestMethod=POST, host=localhost:7575, http_requestUrl=http://localhost:7575/timeout, accept=user-agent=curl/8.4.0}, timestamp=Mon Jun 03 08:02:56 EDT 2024, messageId=f351dcfc-4fb7-4ded-7ffc-49338be5d194, contentType=application/json, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=true, receivedExchange=api-throttle, receivedRoutingKey=api-throttle, deliveryTag=1, consumerTag=amq.ctag-3v0hwHqd7JB2fbET1g8WnQ, consumerQueue=api-throttle.api-throttling])"
            "contentType" -> {MimeType@9366} "application/json"
            "user-agent" -> "curl/8.4.0"
            "timestamp" -> {Long@9370} 1717416310001
         */

    @BeforeEach
    void setUp() {
        subject = new ApiThrottleConsumer(restTemplate,url);
    }

    @Test
    void accept_throwsException() {

        when(restTemplate.exchange(any(URI.class),any(HttpMethod.class),any(),any(Class.class))).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.REQUEST_TIMEOUT);
        message = new GenericMessage<String>(payload,headers);

        assertThrows(CommunicationException.class, () -> subject.accept(message));

    }

    @Test
    void accept_OK() {

        when(restTemplate.exchange(any(URI.class),any(HttpMethod.class),any(),any(Class.class))).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);

        message = new GenericMessage<String>(payload,headers);
        assertDoesNotThrow( () -> subject.accept(message));
    }
}