package com.flixbus.agencyManagement

import com.flixbus.agencyManagement.agency.AgencyEntity
import com.flixbus.agencyManagement.agency.dto.AgencyRequestDto


fun createDummyAgency(): AgencyEntity {
    val dummyAgency = AgencyEntity()
    dummyAgency.code = "code"
    dummyAgency.name = "Le Chamois"
    dummyAgency.country = "France"
    dummyAgency.countryCode = "FRA"
    dummyAgency.city = "Paris"
    dummyAgency.street = "Rue Bonaparte 7"
    dummyAgency.settlementCurrency = "contactPerson"
    dummyAgency.contactPerson = "Madame Beaufort"
    return dummyAgency
}

fun createRequestAgencyDto() = AgencyRequestDto(
    "Le Chamois",
    "France",
    "FRA",
    "Paris",
    "Rue Bonaparte 7",
    "contactPerson",
    "Madame Beaufort"
)