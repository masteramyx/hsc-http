package com.shadowconnect.routes

import com.shadowconnect.db.DatabaseRepositoryImpl
import com.shadowconnect.model.Student
import com.shadowconnect.model.studentStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.studentRouting() {

    //route block sets endpoint and subsequent blocks set http methods
    route("/students") {
        // region get
        /**
         * Return full list of users
         */
        get {
            if (studentStorage.isNotEmpty()) {
                val t = DatabaseRepositoryImpl()
                call.respond(studentStorage)
            } else {
                call.respondText("We have no users", status = HttpStatusCode.OK)
            }
        }

        /**
         * Make basic parameter check and error response for fetching user by id
         */
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val user = studentStorage.find { it.id == id } ?: return@get call.respondText(
                "No customer with id: $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }
        // endregion

        // region post
        /**
         * POST a JSON representation of user object to be stored in `database`
         */
        post {
            val student: Student = call.receive()
            studentStorage.add(student)
            call.respondText(
                "User Stored in `database`", status = HttpStatusCode.Created
            )
        }
        // endregion

        // region delete
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (studentStorage.removeIf { it.id == id }) {
                call.respondText("User removed from `database", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
        // endregion
    }
}