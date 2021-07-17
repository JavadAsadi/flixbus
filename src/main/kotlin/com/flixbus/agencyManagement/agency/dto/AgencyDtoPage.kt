package com.flixbus.agencyManagement.agency.dto

data class AgencyDtoPage(
    val items: List<SearchResponseDto>,
    val pageSize: Int,
    val pageNumber: Int,
    val totalCount: Long
)