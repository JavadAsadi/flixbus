package com.flixbus.agencyManagement.agency

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AgencyEntity {
    @Id
    private var id: String? = null
    @Indexed(unique = true)
    lateinit var code: String
    @Indexed(unique = true)
    lateinit var name: String
    lateinit var country: String
    lateinit var countryCode: String
    lateinit var city: String
    lateinit var street: String
    lateinit var settlementCurrency: String
    lateinit var contactPerson: String
}