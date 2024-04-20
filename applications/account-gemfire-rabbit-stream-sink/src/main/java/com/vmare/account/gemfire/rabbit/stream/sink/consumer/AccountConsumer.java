package com.vmare.account.gemfire.rabbit.stream.sink.consumer;

import com.vmare.account.gemfire.rabbit.stream.sink.repository.AccountRepository;
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@RequiredArgsConstructor
public class AccountConsumer implements Consumer<Account> {
    private final AccountRepository repository;

    @Override
    public void accept(Account account) {
        repository.save(account);
    }
}
