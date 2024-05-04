package showcase.event.stream.rabbitmq.log.sink.functions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class LogConsumer implements Consumer<byte[]> {
    @Override
    public void accept(byte[] bytes) {
        log.info("CONSUMED: {} ",new String(bytes));
    }
}
