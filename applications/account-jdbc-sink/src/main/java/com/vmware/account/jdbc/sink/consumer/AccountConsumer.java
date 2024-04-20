package com.vmware.account.jdbc.sink.consumer;

import com.vmware.account.jdbc.sink.domain.AccountEntity;
import com.vmware.account.jdbc.sink.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

    @Component
    @RequiredArgsConstructor
    public class AccountConsumer implements Consumer<AccountEntity> {

        private final AccountRepository repository;
        @Override
        public void accept(AccountEntity account) {
            repository.save(account);

        }
    }

