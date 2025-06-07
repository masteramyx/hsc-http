package com.shadowconnect.routes

import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.db.StudentRepositoryImpl
import com.shadowconnect.model.Student
import com.shadowconnect.model.CreateStudentRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    val database = DatabaseFactory.getDatabase()
    val studentRepository = StudentRepositoryImpl(database)

    //route block sets endpoint and subsequent blocks set http methods
    route("/students") {
        // region get
        /**
         * Return full list of active students
         */
        get {
            try {
                val students = studentRepository.getAllActiveStudents().map { studentData ->
                    Student(
                        id = studentData.id,
                        userId = studentData.userId,
                        firstName = studentData.firstName,
                        lastName = studentData.lastName,
                        email = studentData.email,
                        phone = studentData.phone,
                        studentId = studentData.studentId,
                        major = studentData.major,
                        yearLevel = studentData.yearLevel,
                        gpa = studentData.gpa,
                        userType = studentData.userType,
                        isActive = studentData.isActive ?: false
                    )
                }
                
                if (students.isNotEmpty()) {
                    call.respond(students)
                } else {
                    call.respondText("No students found", status = HttpStatusCode.OK)
                }
            } catch (e: Exception) {
                call.respondText("Error fetching students: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        /**
         * Get student by ID
         */
        get("{id?}") {
            val idStr = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            
            val id = idStr.toLongOrNull() ?: return@get call.respondText(
                "Invalid id format",
                status = HttpStatusCode.BadRequest
            )

            try {
                val studentData = studentRepository.getStudentById(id) ?: return@get call.respondText(
                    "No student with id: $id",
                    status = HttpStatusCode.NotFound
                )
                
                val student = Student(
                    id = studentData.id,
                    userId = studentData.userId,
                    firstName = studentData.firstName,
                    lastName = studentData.lastName,
                    email = studentData.email,
                    phone = studentData.phone,
                    studentId = studentData.studentId,
                    major = studentData.major,
                    yearLevel = studentData.yearLevel,
                    gpa = studentData.gpa,
                    userType = studentData.userType,
                    isActive = studentData.isActive ?: false
                )
                call.respond(student)
            } catch (e: Exception) {
                call.respondText("Error fetching student: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
        // endregion

        // region post
        /**
         * Create a new student
         */
        post {
            try {
                val request: CreateStudentRequest = call.receive()
                val success = studentRepository.createStudent(
                    userId = request.userId,
                    firstName = request.firstName,
                    lastName = request.lastName,
                    phone = request.phone,
                    studentId = request.studentId,
                    major = request.major,
                    yearLevel = request.yearLevel,
                    gpa = request.gpa
                )
                
                if (success) {
                    call.respondText("Student created successfully", status = HttpStatusCode.Created)
                } else {
                    call.respondText("Failed to create student", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Exception) {
                call.respondText("Error creating student: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }
        // endregion
    }
}