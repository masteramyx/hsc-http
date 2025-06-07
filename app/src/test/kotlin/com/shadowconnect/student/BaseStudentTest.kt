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
        Student(1L, 1L, "Kyle", "Franklin", "abc@abc.com", "555-0123", "STU001", "Computer Science", 3, 3.75, "student", true),
        Student(2L, 2L, "Kyle", "Franklin", "abc@abc.com", "555-0124", "STU002", "Biology", 2, 3.80, "student", true),
        Student(3L, 3L, "Kyle", "Franklin", "abc@abc.com", "555-0125", "STU003", "Physics", 4, 3.65, "student", true)
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