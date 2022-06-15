package com.shadowconnect.student

import com.shadowconnect.model.Student
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Tests for all user endpoints.
 * There should both success and failure tests for all endpoints.
 */
class StudentRouteTest : BaseStudentTest() {

    // region setup
    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
    // endregion


    //TODO setup DB
//    @Test
//    fun testGetAllUsersEmpty() = testApplication {
//        val response = mockClient.get("/users")
//        assertEquals(HttpStatusCode.OK, response.status)
//        assertEquals("We have no users", response.bodyAsText())
//    }


    @Test
    fun testGetAllUsers() = testApplication {
        val response = this@StudentRouteTest.mockClient.get("/students")
        assertEquals(HttpStatusCode.OK, response.status)
        val userResponse = response.bodyAsText()
        println(userResponse)
        val obj = Json.decodeFromString<List<Student>>(userResponse)
        assertEquals(3, obj.size)
    }
}

