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
import com.shadowconnect.utils.logger

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
            logger.debug("GET /students - Fetching all active students")
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
                    logger.debug("GET /students - Returning ${students.size} students")
                    call.respond(students)
                } else {
                    logger.debug("GET /students - No students found")
                    call.respondText("No students found", status = HttpStatusCode.OK)
                }
            } catch (e: Exception) {
                logger.error("GET /students - Error fetching students: ${e.message}", e)
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

            logger.debug("GET /students/$id - Fetching student by ID")
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
                logger.debug("GET /students/$id - Student found and returned")
                call.respond(student)
            } catch (e: Exception) {
                logger.error("GET /students/$id - Error fetching student: ${e.message}", e)
                call.respondText("Error fetching student: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
        // endregion

        // region post
        /**
         * Create a new student
         */
        post {
            logger.debug("POST /students - Creating new student")
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
                    logger.info("POST /students - Student created successfully")
                    call.respondText("Student created successfully", status = HttpStatusCode.Created)
                } else {
                    logger.warn("POST /students - Failed to create student")
                    call.respondText("Failed to create student", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Exception) {
                logger.error("POST /students - Error creating student: ${e.message}", e)
                call.respondText("Error creating student: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }
        // endregion
    }
}