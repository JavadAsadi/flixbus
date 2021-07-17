package com.flixbus.agencyManagement.agency.dto

data class AgencyLoadResponseDto(
    val code: String,
    val name: String,
    val country: String,
    val countryCode: String,
    val city: String,
    val street: String,
    val settlementCurrency: String,
    val contactPerson: String
)