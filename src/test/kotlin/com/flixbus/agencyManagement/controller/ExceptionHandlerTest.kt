package com.flixbus.agencyManagement.controller

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class ExceptionHandlerTest {

    private val agencyExceptionHandler = ExceptionHandler()

    @Test
    fun assert_whenNoAgencyIsFound_responseEntityWithStatusOf_NotFound_IsCreated() {
        val exception = IllegalStateException("")

        val status = agencyExceptionHandler.handleNoAgencyFoundException(exception).statusCode

        kotlin.test.assertEquals(HttpStatus.NOT_FOUND, status)
    }

}