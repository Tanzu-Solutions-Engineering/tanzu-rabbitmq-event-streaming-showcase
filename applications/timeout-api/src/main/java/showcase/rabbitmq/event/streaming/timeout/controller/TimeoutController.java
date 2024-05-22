package showcase.rabbitmq.event.streaming.timeout.controller;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpTimeoutException;

@RestController
public class TimeoutController {

    @PostMapping("timeout")
    public ResponseEntity<Account> timeout(@RequestBody Account account) throws HttpTimeoutException {
        throw new HttpTimeoutException("TESTING");
    }
}
