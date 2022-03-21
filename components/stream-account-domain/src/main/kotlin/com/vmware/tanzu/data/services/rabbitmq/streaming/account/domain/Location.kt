package com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain

data class Location(
    var id: String = "",
    var address: String= "",
    var cityTown: String = "",
    var stateProvince: String = "",
    var zipPostalCode: String = "",
    var countryCode : String = ""
)
