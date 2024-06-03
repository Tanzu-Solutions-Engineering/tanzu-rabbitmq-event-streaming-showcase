package showcase.rabbitmq.event.streaming.timeout.controller;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Demo code for testing API throttling
 */
@RestController
@Slf4j
public class TimeoutController {

    @PostMapping("timeout")
    public ResponseEntity<Account> timeout(@RequestBody Account account) throws HttpTimeoutException {

        //Throw
        if ("TIMEOUT".equals(account.getStatus()))
        {
            log.error("RETURN timeout error based on status for account : {}",account);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }

        int second = LocalDateTime.now().get(ChronoField.SECOND_OF_DAY);
        if(second %2 != 0)
        {
            var timeoutException =new HttpTimeoutException("TESTING");
            log.error("Cannot process account: % THROWING EXCEPTION: %",account, timeoutException);
            throw timeoutException;
        }

        var ok = ok(account);
        log.info("GOOD REPLAY: {}",ok);
        return ok;
    }
}
