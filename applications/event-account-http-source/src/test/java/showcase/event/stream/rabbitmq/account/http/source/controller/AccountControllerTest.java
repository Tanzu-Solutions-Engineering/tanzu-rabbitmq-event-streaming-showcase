package showcase.event.stream.rabbitmq.account.http.source.controller;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.integration.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private Account account = JavaBeanGeneratorCreator.of(Account.class).create();
    private AccountController subject;

    @Mock
    private Publisher<Account> publisher;

    @BeforeEach
    void setUp() {
        subject = new AccountController(publisher);
    }

    @Test
    void publish() {

        subject.publish(account);

        verify(publisher).send(account);
    }
}