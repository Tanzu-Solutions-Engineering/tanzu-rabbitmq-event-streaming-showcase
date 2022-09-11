package com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain

import javax.persistence.*

@Entity
@Table(name = "evt_accounts")
data class AccountEntity(@Id var id : String = "",
                         var name: String = "",
                         var accountType : String = "",
                         var status : String  = "",
                         var notes : String = "",
                         @OneToOne(cascade = [CascadeType.ALL])
                   var location: LocationEntity = LocationEntity()
)
