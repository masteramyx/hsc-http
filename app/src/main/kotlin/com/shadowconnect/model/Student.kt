package com.shadowconnect.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    @SerialName("id")
    val id: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("email")
    val email: String
)

//todo store this in database...firebase?
val studentStorage = mutableListOf(
    Student("1", "Kyle", "Franklin", "abc@abc.com"),
    Student("2", "Kyle", "Franklin", "abc@abc.com"),
    Student("3", "Kyle", "Franklin", "abc@abc.com")
)