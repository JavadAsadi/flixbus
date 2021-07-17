package com.flixbus.agencyManagement.controller

import com.flixbus.agencyManagement.agency.AgencyService
import com.flixbus.agencyManagement.agency.dto.AgencyPaginationParam
import com.flixbus.agencyManagement.agency.dto.AgencyRequestDto
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class AgencyControllerUnitTest {

    private val service = mockk<AgencyService>()
    private val controller = AgencyController(service)

    @Test
    fun whenLoadAgencyOnControllerIsCalled_loadAgencyByCodeOnServiceShouldCalled() {
        every { service.loadByCode(any()) } returns mockk()

        controller.loadAgency("code")

        verify(exactly = 1) { service.loadByCode("code") }
    }

    @Test
    fun controllerUsesTheSameCodeItReceivesToLoadAgency() {
        val code = slot<String>()
        every { service.loadByCode(capture(code)) } returns mockk()

        controller.loadAgency("code")

        assertEquals("code", code.captured)
    }

    @Test
    fun whenSaveAgencyOnControllerIsCalled_saveAgencyOnServiceShouldBeCalled() {
        every { service.saveAgency(any()) } returns mockk()

        controller.saveAgency(mockk())

        verify(exactly = 1) { service.saveAgency(any()) }
    }

    @Test
    fun whenDeleteAgencyOnControllerIsCalled_deleteAgencyOnServiceShouldBeCalled() {
        every { service.deleteByCode("code") } just  Runs

        controller.deleteAgency("code")

        verify(exactly = 1) { service.deleteByCode("code") }
    }

    @Test
    fun controllerUsesTheSameCodeItReceivesToDeleteAgency() {
        val code = slot<String>()
        every { service.deleteByCode(capture(code)) } just Runs

        controller.deleteAgency("code")

        assertEquals("code", code.captured)
    }

    @Test
    fun whenUpdateAgencyOnControllerIsCalled_updateAgencyOnServiceShouldBeCalled() {
        val updateReq = mockk<AgencyRequestDto>()
        every { service.updateAgency("code", updateReq) } just Runs

        controller.updateAgency("code", updateReq)

        verify(exactly = 1) { service.updateAgency("code", updateReq) }
    }

    @Test
    fun whenRetrieveOnControllerIsCalled_retrieveOnServiceShouldBeCalled() {
        val emptyParamRequest = AgencyPaginationParam()
        every { service.retrieve(emptyParamRequest) } returns mockk()

        controller.retrieve(emptyParamRequest)

        verify(exactly = 1) { service.retrieve(emptyParamRequest) }
    }

}