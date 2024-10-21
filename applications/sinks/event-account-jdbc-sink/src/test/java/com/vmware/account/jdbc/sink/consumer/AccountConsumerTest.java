package com.vmware.account.jdbc.sink.consumer;

import com.vmware.account.jdbc.sink.domain.AccountEntity;
import com.vmware.account.jdbc.sink.repository.AccountRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountConsumerTest {

    private AccountConsumer subject;
    @Mock
    private AccountRepository repository;
    private AccountEntity account = JavaBeanGeneratorCreator.of(AccountEntity.class).create();

    @BeforeEach
    void setUp() {
        subject = new AccountConsumer(repository);
    }

    @Test
    void accept() {
        subject.accept(account);

        verify(repository).save(any());
    }
}