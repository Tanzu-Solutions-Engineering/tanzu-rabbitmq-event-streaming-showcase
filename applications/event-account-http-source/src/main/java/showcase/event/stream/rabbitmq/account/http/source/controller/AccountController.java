package showcase.event.stream.rabbitmq.account.http.source.controller;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class AccountController {
    private final RabbitStreamTemplate template;

    @PostMapping
    public void publish(@RequestBody Account account) {
        template.convertAndSend(account);
    }
}
