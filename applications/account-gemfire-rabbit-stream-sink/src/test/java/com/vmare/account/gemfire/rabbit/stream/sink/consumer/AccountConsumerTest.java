package com.vmare.account.gemfire.rabbit.stream.sink.consumer;

import com.vmare.account.gemfire.rabbit.stream.sink.repository.AccountRepository;
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountConsumerTest {

    private AccountConsumer subject;
    @Mock
    private AccountRepository repository;
    private Account account = JavaBeanGeneratorCreator.of(Account.class).create();

    @Test
    void accept() {
        subject = new AccountConsumer(repository);

        subject.accept(account);

        verify(repository).save(any());

    }
}