package com.vmware.account.jdbc.sink.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evt_locations",schema="evt_showcase")
@Data
@NoArgsConstructor
public class LocationEntity {
    @Id
    private String id;
    private String address;
    private String cityTown;
    private String stateProvince;
    private String zipPostalCode;
    private String countryCode;
}
