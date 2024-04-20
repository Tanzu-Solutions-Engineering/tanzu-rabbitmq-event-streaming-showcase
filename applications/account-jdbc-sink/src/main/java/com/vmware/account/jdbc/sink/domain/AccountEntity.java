package com.vmware.account.jdbc.sink.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evt_accounts")
@Data
@NoArgsConstructor
public class AccountEntity {
    @Id
    private String id;

    private String name;
    private String accountType;
    private String status;
    private String notes;

    @OneToOne(cascade = CascadeType.ALL )
    private LocationEntity location ;

}
