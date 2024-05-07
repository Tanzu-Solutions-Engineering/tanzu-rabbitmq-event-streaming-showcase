package com.vmware.account.jdbc.sink.consumer;

import com.vmware.account.jdbc.sink.domain.AccountEntity;
import com.vmware.account.jdbc.sink.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class AccountConsumer implements Consumer<AccountEntity> {

        private final AccountRepository repository;
        @Override
        public void accept(AccountEntity account) {
            repository.save(account);

        }
    }

