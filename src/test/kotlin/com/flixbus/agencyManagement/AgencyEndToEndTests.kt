package com.flixbus.agencyManagement

import com.flixbus.agencyManagement.agency.dto.AgencyRequestDto
import com.flixbus.agencyManagement.agency.dto.SaveResponseDto
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.BeforeTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AgencyEndToEndTests {
    @LocalServerPort
    private var port: Int = 0

    private lateinit var webTestClient: WebTestClient
    private lateinit var webClient: WebClient
    private val requestWithAllProperties = createRequestAgencyDto()

    @BeforeTest
    fun setUp() {
        val baseUrl = "http://localhost:$port/flix/api/agency"
        fun initialWebTestClient() = WebTestClient
            .bindToServer()
            .baseUrl(baseUrl)
            .build()
        webTestClient = initialWebTestClient()
        webClient = WebClient.create(baseUrl)
    }

    @Test
    fun testStatusCode_whenSavingAgency() {
        webTestClient
            .post()
            .bodyValue(requestWithAllProperties)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun testStatus_whenLoadingNonExistentAgency() {
        webTestClient
            .get()
            .uri("/randomCode")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun testResponse_whenLoadingExistingAgency() {
        with(requestWithAllProperties) {
            val codeOfAgencyAfterBeingSaved = saveAnAgency(this)
            webTestClient
                .get()
                .uri("/$codeOfAgencyAfterBeingSaved")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("code").isEqualTo(codeOfAgencyAfterBeingSaved)
                .jsonPath("name").isEqualTo(name)
                .jsonPath("city").isEqualTo(city)
                .jsonPath("country").isEqualTo(country)
                .jsonPath("countryCode").isEqualTo(countryCode)
                .jsonPath("contactPerson").isEqualTo(contactPerson)
                .jsonPath("settlementCurrency").isEqualTo(settlementCurrency)
                .jsonPath("street").isEqualTo(street)
        }
    }

    @Test
    fun testResponse_whenUpdatingExistingAgencyWithCompleteProperties() {
        val codeOfAgencyAfterBeingSaved = saveAnAgency(requestWithAllProperties)
        webTestClient
            .put()
            .uri("/$codeOfAgencyAfterBeingSaved")
            .bodyValue(requestWithAllProperties)
            .exchange()
            .expectStatus().isNoContent
    }

    private fun saveAnAgency(dummySaveRequest: AgencyRequestDto): String = webClient
        .post()
        .bodyValue(dummySaveRequest)
        .retrieve()
        .toEntity(SaveResponseDto::class.java)
        .block()!!.body!!.code
}