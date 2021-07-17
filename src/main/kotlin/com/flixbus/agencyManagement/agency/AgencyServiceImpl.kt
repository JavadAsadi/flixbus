package com.flixbus.agencyManagement.agency

import com.flixbus.agencyManagement.agency.dto.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class AgencyServiceImpl(private val repository: AgencyRepository) : AgencyService {
    override fun loadByCode(code: String): AgencyLoadResponseDto {
        fun convertToDto(entity : AgencyEntity) = AgencyLoadResponseDto(
            name = entity.name,
            code = entity.code,
            city = entity.city,
            street = entity.street,
            contactPerson = entity.contactPerson,
            settlementCurrency = entity.settlementCurrency,
            country = entity.country,
            countryCode = entity.countryCode
        )
        val agency = loadAgencyByCode(code)
        return convertToDto(agency)
    }

    override fun saveAgency(agencySaveRequestDto: AgencyRequestDto): SaveResponseDto {
        fun convertToEntity(req : AgencyRequestDto): AgencyEntity{
                val entity = AgencyEntity()
                entity.name = req.name
                entity.city = req.city
                entity.country = req.country
                entity.countryCode = req.countryCode
                entity.contactPerson = req.contactPerson
                entity.settlementCurrency = req.settlementCurrency
                entity.street = req.street
                return entity
            }
        val entity = convertToEntity(agencySaveRequestDto)
        entity.code = UUID.randomUUID().toString()
        repository.save(entity)
        return SaveResponseDto(entity.code)
    }

    override fun deleteByCode(code: String) {
        val entity = loadAgencyByCode(code)
        repository.delete(entity)
    }

    override fun updateAgency(code: String, agencyUpdateRequestDto: AgencyRequestDto) {
        val oldEntity = loadAgencyByCode(code)

        oldEntity.contactPerson = agencyUpdateRequestDto.contactPerson
        oldEntity.countryCode = agencyUpdateRequestDto.countryCode
        oldEntity.country = agencyUpdateRequestDto.country
        oldEntity.city = agencyUpdateRequestDto.city
        oldEntity.street = agencyUpdateRequestDto.street
        oldEntity.settlementCurrency = agencyUpdateRequestDto.settlementCurrency
        oldEntity.name = agencyUpdateRequestDto.name

        repository.save(oldEntity)
    }

    override fun retrieve(paginationParams: AgencyPaginationParam): AgencyDtoPage {
        fun convertToSearchResponse(e: AgencyEntity): SearchResponseDto =
            SearchResponseDto(e.code, e.name, e.contactPerson, e.country, e.city)
        val pageRequest = with(paginationParams) {PageRequest.of(pageNumber-1, pageSize, Sort.by(direction, orderBy))}
        return AgencyDtoPage(
            repository.findAll(pageRequest).content.map (::convertToSearchResponse),
            paginationParams.pageSize,
            paginationParams.pageNumber,
            repository.count()
        )
    }

    private fun loadAgencyByCode(code: String) : AgencyEntity {
        val entity = repository.findByCode(code)
        checkNotNull(entity){"the requested agency with code : $code was not found"}
        return entity
    }
}