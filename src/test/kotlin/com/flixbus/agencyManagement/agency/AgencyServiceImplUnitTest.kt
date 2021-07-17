package com.flixbus.agencyManagement.agency

import com.flixbus.agencyManagement.agency.dto.AgencyPaginationParam
import com.flixbus.agencyManagement.createDummyAgency
import com.flixbus.agencyManagement.createRequestAgencyDto
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import kotlin.test.assertFailsWith

internal class AgencyServiceImplUnitTest {

    private val agencyRepository = mockk<AgencyRepository>()
    private val agencyService = AgencyServiceImpl(agencyRepository)

    @Test
    fun whenLoadAgencyByCodeIsCalled_findByCodeInRepositoryShouldBeCalled() {
        val expectedEntity = createDummyAgency()
        every { agencyRepository.findByCode(any()) } returns expectedEntity

        agencyService.loadByCode("code")

        verify(exactly = 1) { agencyRepository.findByCode("code") }
    }

    @Test
    fun loadAgencyCallsRepositoryByExactCodeItReceivesAsArgument() {
        val codeSlot = slot<String>()
        val expectedEntity = createDummyAgency()
        every { agencyRepository.findByCode(capture(codeSlot)) } returns expectedEntity

        agencyService.loadByCode("code")

        assertEquals("code", codeSlot.captured)
    }

    @Test
    fun illegalStateExceptionIsThrownWhenRepositoryReturnsNull() {
        every { agencyRepository.findByCode(any()) } returns null

        assertFailsWith<IllegalStateException> {
            agencyService.loadByCode("randomCode")
        }
    }

    @Test
    fun loadAgencyByCodeReturnsCorrectDto() {
        val expectedEntity = createDummyAgency()
        every { agencyRepository.findByCode((any())) } returns expectedEntity

        val resultAgencyDto = agencyService.loadByCode("code")

        assertAll(
            { assertEquals(expectedEntity.name, resultAgencyDto.name, "name") },
            {assertEquals(expectedEntity.contactPerson, resultAgencyDto.contactPerson, "contactPerson")},
            {assertEquals(expectedEntity.city, resultAgencyDto.city, "city")},
            {assertEquals(expectedEntity.street, resultAgencyDto.street, "street")},
            {assertEquals(expectedEntity.settlementCurrency, resultAgencyDto.settlementCurrency, "settlementCurrency")},
            {assertEquals(expectedEntity.country, resultAgencyDto.country, "country")},
            {assertEquals(expectedEntity.countryCode, resultAgencyDto.countryCode, "countryCode")}
        )
    }

    @Test
    fun whenSaveAgencyIsCalled_saveInRepositoryShouldBeCalled() {
        val expectedEntity = createDummyAgency()
        every { agencyRepository.save(any()) } returns expectedEntity

        agencyService.saveAgency(createRequestAgencyDto())

        verify(exactly = 1) { agencyRepository.save(any()) }
    }

    @Test
    fun whenSearchIsCalled_findAllAndCountOnRepositoryShouldBeCalled() {
        val paginationParam = AgencyPaginationParam()
        every { agencyRepository.findAll(any<Pageable>()) } returns Page.empty()
        every { agencyRepository.count() } returns 0

        agencyService.retrieve(paginationParam)

        assertAll(
            { verify(exactly = 1) { agencyRepository.findAll(any<Pageable>()) } },
            { verify(exactly = 1) { agencyRepository.count() } }
        )


    }

    @Test
    fun whenUpdateAgencyIsCalled_saveAndFindByIdAgencyInRepositoryShouldBeCalled() {
        val oldEntity = createDummyAgency()
        every { agencyRepository.save(any()) } returns mockk()
        every { agencyRepository.findByCode("code") } returns oldEntity

        agencyService.updateAgency(oldEntity.code, createRequestAgencyDto())

        assertAll(
            { verify(exactly = 1) { agencyRepository.findByCode(oldEntity.code) } },
            { verify(exactly = 1) { agencyRepository.save(any()) } }
        )
    }

    @Test
    fun whenUpdatingNonExistentAgency_illegalStateExceptionIsThrown() {
        every { agencyRepository.findByCode(any()) } returns null

        assertFailsWith<IllegalStateException> {
            agencyService.updateAgency("code", createRequestAgencyDto())
        }
    }

    @Test
    fun whenDeleteAgencyIsCalled_findByCodeAndDeleteOnAgencyIsCalled() {
        every { agencyRepository.findByCode(any()) } returns mockk()
        every { agencyRepository.delete(any()) } just runs

        agencyService.deleteByCode("randomCode")

        assertAll(
            { verify(exactly = 1) { agencyRepository.findByCode("randomCode") } },
            { verify(exactly = 1) { agencyRepository.delete(any()) } }
        )
    }


}