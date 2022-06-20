package com.shadowconnect.student

import com.shadowconnect.model.Student
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
abstract class BaseStudentTest {
    lateinit var mockClient: HttpClient


    @Before
    open fun setUp() {
        mockClient = createMockClient()
    }

    @After
    open fun tearDown() {
        mockClient.close()
    }

    val testStudents = listOf<Student>(
        Student("1", "Kyle", "Franklin", "abc@abc.com"),
        Student("2", "Kyle", "Franklin", "abc@abc.com"),
        Student("3", "Kyle", "Franklin", "abc@abc.com")
    )


    private fun createMockClient(): HttpClient =
        HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when ((request.url.encodedPathAndQuery)) {
                        "/students" -> {
                            respond(
                                Json.encodeToString(testStudents),
                                HttpStatusCode.OK,
                                headersOf("content-Type", ContentType.Application.Json.toString())
                            )
                        }
                        else -> error("NO!: ${request.url.toString()}")
                    }
                }
            }
        }
}