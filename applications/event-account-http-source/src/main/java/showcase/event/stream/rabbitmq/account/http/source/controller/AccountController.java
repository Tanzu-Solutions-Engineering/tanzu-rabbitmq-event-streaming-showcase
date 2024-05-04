package showcase.event.stream.rabbitmq.account.http.source.controller;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import lombok.RequiredArgsConstructor;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.integration.Publisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class AccountController {
    private final Publisher<Account> publisher;
    private Converter<Account,byte[]> converter;

    @PostMapping
    public void publish(@RequestBody Account account) {
        publisher.send(account);
    }
}
