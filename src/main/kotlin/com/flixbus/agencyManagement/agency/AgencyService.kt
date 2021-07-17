package com.flixbus.agencyManagement.agency

import com.flixbus.agencyManagement.agency.dto.*

interface AgencyService {
    fun loadByCode(code: String): AgencyLoadResponseDto
    fun saveAgency(agencySaveRequestDto: AgencyRequestDto): SaveResponseDto
    fun deleteByCode(code: String)
    fun updateAgency(code: String, agencyUpdateRequestDto: AgencyRequestDto)
    fun retrieve(paginationParams: AgencyPaginationParam): AgencyDtoPage
}