package com.vmare.account.gemfire.rabbit.stream.sink.repository;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,String> {
}
