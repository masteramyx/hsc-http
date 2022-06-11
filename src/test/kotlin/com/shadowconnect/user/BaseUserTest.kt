package com.shadowconnect.user

import com.shadowconnect.model.User
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before


/**
 *
 *
 *
 * //TODO @see [https://github.com/mockk/mockk/issues/571]
 * This issue led to decision to use MockEngine. Maybe circle back if
 * mockk makes sense over MockEngine.
 */
abstract class BaseUserTest {
    lateinit var mockClient: HttpClient


    @Before
    open fun setUp() {
        mockClient = createMockClient()
    }

    @After
    open fun tearDown() {
        mockClient.close()
    }

    val testUsers = listOf<User>(
        User("1", "Kyle", "Franklin", "abc@abc.com"),
        User("2", "Kyle", "Franklin", "abc@abc.com"),
        User("3", "Kyle", "Franklin", "abc@abc.com")
    )


    fun createMockClient(): HttpClient =
        HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when ((request.url.encodedPathAndQuery)) {
                        "/users" -> {
                            // return empty list or not for test?
                            if (request.url.parameters.contains("empty")) {
                                respond(
                                    Json.encodeToString(emptyList<User>()),
                                    HttpStatusCode.OK,
                                    headersOf("content-Type", ContentType.Application.Json.toString())
                                )
                            } else {
                                respond(
                                    Json.encodeToString(testUsers),
                                    HttpStatusCode.OK,
                                    headersOf("content-Type", ContentType.Application.Json.toString())
                                )
                            }
                        }
                        else -> error("NO!: ${request.url.toString()}")
                    }
                }
            }
        }
}