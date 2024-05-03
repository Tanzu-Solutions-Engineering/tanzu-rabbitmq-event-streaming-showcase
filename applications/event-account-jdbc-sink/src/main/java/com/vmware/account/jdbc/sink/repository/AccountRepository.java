package com.vmware.account.jdbc.sink.repository;

import com.vmware.account.jdbc.sink.domain.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity,String> {
}
