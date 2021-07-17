package com.flixbus.agencyManagement.agency.dto

import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC

data class AgencyPaginationParam(
    val pageSize: Int = 10,
    val pageNumber: Int = 1,
    val orderBy: String = "name",
    val direction: Sort.Direction = ASC
)