package com.shadowconnect

import com.shadowconnect.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserRouteTests {

    // region setup
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
    // endregion

    val testUsers = listOf<User>(
        User("1", "Kyle", "Franklin", "abc@abc.com"),
        User("1", "Kyle", "Franklin", "abc@abc.com"),
        User("1", "Kyle", "Franklin", "abc@abc.com")
    )

    @RelaxedMockK
    lateinit var mockClient:HttpClient


    @Test
    fun testGetAllUsersEmpty() = testApplication {
        val response = client.get("/users")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("We have no users", response.bodyAsText())
    }



    @Test
    fun debugTest() {
        class Adder {
            fun addOne(num: Int) = num + 1
        }

        val adder = mockk<Adder>()

        every { adder.addOne(any()) } returns -1
        every { adder.addOne(3) } answers { callOriginal() }

        assertEquals(-1, adder.addOne(2))
        assertEquals(4, adder.addOne(3)) // original function is called
    }

    @Test
    fun testGetAllUsers() = testApplication {

        val m = mockk<HttpClient>()
        every { m.get("/") } returns testUsers


        val response = client.get("/users") {
            setBody(testUsers.toString())
        }
        assertEquals(HttpStatusCode.OK, response.status)
        print(response.bodyAsText())
        assertEquals(3, (response.body() as List<User>).size)
    }

}