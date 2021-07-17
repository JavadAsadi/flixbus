package com.flixbus.agencyManagement.agency

import org.springframework.data.mongodb.repository.MongoRepository

interface AgencyRepository : MongoRepository<AgencyEntity, String> {
    fun findByCode(code : String) : AgencyEntity?
}