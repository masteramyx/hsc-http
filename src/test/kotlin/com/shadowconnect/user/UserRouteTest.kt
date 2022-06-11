package com.shadowconnect.user

import com.shadowconnect.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Tests for all user endpoints.
 * There should both success and failure tests for all endpoints.
 */
class UserRouteTest : BaseUserTest() {

    // region setup
    @Before
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)
    }

    @After
    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }
    // endregion



    @Test
    fun testGetAllUsersEmpty() = testApplication {
        val response = mockClient.get("/users") {
            this.parameter("empty", true)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("We have no users", response.bodyAsText())
    }


    @Test
    fun testGetAllUsers() = testApplication {
        val response = this@UserRouteTest.mockClient.get("/users") {}
        assertEquals(HttpStatusCode.OK, response.status)
        val s = response.bodyAsText()
        println(s)
        val obj = Json.decodeFromString<List<User>>(s)
        assertEquals(3, obj.size)
    }
}

