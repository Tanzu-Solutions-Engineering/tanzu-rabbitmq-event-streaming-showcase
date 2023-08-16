package com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "evt_locations")
data class LocationEntity(
    @Id var id: String = "",
    var address: String= "",
    var cityTown: String = "",
    var stateProvince: String = "",
    var zipPostalCode: String = "",
    var countryCode : String = ""
)
