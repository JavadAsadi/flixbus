package com.flixbus.agencyManagement.agency

import com.flixbus.agencyManagement.agency.dto.AgencyPaginationParam
import com.flixbus.agencyManagement.agency.dto.AgencyRequestDto
import com.flixbus.agencyManagement.createRequestAgencyDto
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AgencyServiceIntegrationTest {

    @Autowired
    private lateinit var agencyService: AgencyService
    private lateinit var requestDto1: AgencyRequestDto
    private lateinit var requestDto2: AgencyRequestDto
    private lateinit var requestDto3: AgencyRequestDto


    @BeforeEach
    fun setUp() {
        requestDto1 = AgencyRequestDto("name1", "country1", "CC1", "city1", "street1", "Rial", "cName1")
        requestDto2 = AgencyRequestDto("name12", "country12", "CC2", "city12", "street2", "Dollar", "cName2")
        requestDto3 = AgencyRequestDto("name3", "country3", "CC3", "city3", "street3", "EURO", "cName3")
    }

    private val requestDto = createRequestAgencyDto()

    @Test
    fun whenLoading_ifAgencyExists_noExceptionIsThrown() {
        val codeRetrievedBySavingAgency = agencyService.saveAgency(requestDto).code
        assertDoesNotThrow { agencyService.loadByCode(codeRetrievedBySavingAgency) }
    }

    @Test
    fun whenLoading_ifAgencyDoesNotExist_AgencyNotFoundExceptionIsThrown() {
        assertFailsWith<IllegalStateException> {
            agencyService.loadByCode("randomCode")
        }
    }

    @Test
    fun testSaveAndLoadIntegrity() {
        val codeRetrievedBySavingAgency = agencyService.saveAgency(requestDto).code
        val loadResponseDto = agencyService.loadByCode(codeRetrievedBySavingAgency)

        assertAll(
            { assertEquals(codeRetrievedBySavingAgency, loadResponseDto.code, "code") },
            { assertEquals(requestDto.name, loadResponseDto.name, "name") },
            { assertEquals(requestDto.city, loadResponseDto.city, "city") },
            { assertEquals(requestDto.settlementCurrency, loadResponseDto.settlementCurrency, "settlementCurrency") },
            { assertEquals(requestDto.country, loadResponseDto.country, "country") },
            { assertEquals(requestDto.countryCode, loadResponseDto.countryCode, "countryCode") },
            { assertEquals(requestDto.contactPerson, loadResponseDto.contactPerson, "contactPerson") },
            { assertEquals(requestDto.street, loadResponseDto.street, "street") },
        )
    }

    @Test
    fun whenDeleting_ifAgencyExists_noExceptionIsThrown() {
        val codeRetrievedBySavingAgency = agencyService.saveAgency(requestDto).code
        assertDoesNotThrow { agencyService.deleteByCode(codeRetrievedBySavingAgency) }
    }

    @Test
    fun whenAgencyIsDeleted_anotherRequestToLoadItFailsWithAgencyNotFoundException() {
        val codeRetrievedBySavingAgency = agencyService.saveAgency(requestDto).code
        agencyService.deleteByCode(codeRetrievedBySavingAgency)

        assertFailsWith<IllegalStateException> {
            agencyService.loadByCode(codeRetrievedBySavingAgency)
        }
    }

    @Test
    fun whenUpdating_ifAgencyDoesNotExist_AgencyNotFoundExceptionIsThrown() {
        assertFailsWith<java.lang.IllegalStateException> {
            agencyService.updateAgency("randomCode", mockk())
        }
    }

    @Test
    fun whenUpdatingExistingAgencyWithCompleteRequestNoExceptionIsThrown() {
        val codeRetrievedBySavingAgency = agencyService.saveAgency(requestDto).code
        assertDoesNotThrow {
            agencyService.updateAgency(codeRetrievedBySavingAgency, requestDto)
        }
    }

    @Test
    fun afterUpdate_AgencyHasNewValues() {
        val codeRetrievedBySavingAgency = agencyService.saveAgency(requestDto).code
        val newValues = AgencyRequestDto(
            "newName", "newCountry", "NWC",
            "newCity", "newStreet", "Rial", "person"
        )

        agencyService.updateAgency(codeRetrievedBySavingAgency, newValues)
        val response = agencyService.loadByCode(codeRetrievedBySavingAgency)


        assertAll(
            { assertEquals(codeRetrievedBySavingAgency, response.code, "code") },
            { assertEquals(newValues.name, response.name, "name") },
            { assertEquals(newValues.city, response.city, "city") },
            { assertEquals(newValues.settlementCurrency, response.settlementCurrency, "settlementCurrency") },
            { assertEquals(newValues.country, response.country, "country") },
            { assertEquals(newValues.countryCode, response.countryCode, "countryCode") },
            { assertEquals(newValues.contactPerson, response.contactPerson, "contactPerson") },
            { assertEquals(newValues.street, response.street, "street") },
        )

    }

    @Test
    fun testDefaultSearchRequest() {
        val searchResponse = agencyService.retrieve(AgencyPaginationParam())

        assertEquals(10, searchResponse.pageSize)
        assertEquals(1, searchResponse.pageNumber)
    }

    @Test
    fun emptySearchRequest_shouldReturnDataOrderedByName() {
        saveAgencies()
        val searchResponseItems = agencyService.retrieve(AgencyPaginationParam()).items
        val names = arrayOf(requestDto1.name, requestDto2.name, requestDto3.name)
        names.sort()

        assertAll(
            { assertEquals(names[0], searchResponseItems[0].name) },
            { assertEquals(names[1], searchResponseItems[1].name) },
            { assertEquals(names[2], searchResponseItems[2].name) }
        )
    }

    @Test
    fun emptySearchRequest_shouldNotHaveAnyLimit() {
        saveAgencies()

        val searchResponse = agencyService.retrieve(AgencyPaginationParam())

        assertAll(
            {assertEquals(3, searchResponse.items.size)},
            {assertEquals(3, searchResponse.totalCount)})
    }

    @Test
    fun itemsShouldBeLimitedBasedOnPagination_totalCountShouldRetrieveNumberOfAll() {
        saveAgencies()
        val searchResponse = agencyService.retrieve(AgencyPaginationParam(pageNumber = 2, pageSize = 1))
        val names = arrayOf(requestDto1.name, requestDto2.name, requestDto3.name)
        names.sort()

        assertAll(
            { assertEquals(1, searchResponse.items.size) },
            { assertEquals(3, searchResponse.totalCount) },
            { assertEquals(names[1], searchResponse.items[0].name)})

    }

    private fun saveAgencies() {
        agencyService.saveAgency(requestDto3)
        agencyService.saveAgency(requestDto2)
        agencyService.saveAgency(requestDto1)
    }

}