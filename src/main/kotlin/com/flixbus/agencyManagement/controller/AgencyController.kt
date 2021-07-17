package com.flixbus.agencyManagement.controller


import com.flixbus.agencyManagement.agency.*
import com.flixbus.agencyManagement.agency.dto.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("flix/api/agency", produces = ["application/json"])
class AgencyController(private val service: AgencyService) {

	@GetMapping("{code}")
	fun loadAgency(@PathVariable code: String): AgencyLoadResponseDto {
		return service.loadByCode(code)
	}

    @PostMapping
    fun saveAgency(@RequestBody agencySaveRequestDto: AgencyRequestDto): SaveResponseDto {
        return service.saveAgency(agencySaveRequestDto)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{code}")
    fun deleteAgency(@PathVariable code: String) {
        service.deleteByCode(code)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{code}")
    fun updateAgency(@PathVariable code: String, @RequestBody agencyUpdateRequestDto: AgencyRequestDto) {
        service.updateAgency(code, agencyUpdateRequestDto)
    }

    @GetMapping
    fun retrieve(paginationParam: AgencyPaginationParam): AgencyDtoPage {
        return service.retrieve(paginationParam)
    }
}