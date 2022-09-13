package com.shadowconnect.routes

import com.shadowconnect.model.Professional
import com.shadowconnect.model.Student
import com.shadowconnect.plugins.db
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.professionalRouting() {

    route("/professionals") {
        // region get
        /**
         * Get full list of professionals
         */
        get {
            val profFromDb = db.getProfessionals()
            if (profFromDb.isNotEmpty()) {
                call.respond(profFromDb)
            } else {
                call.respondText("We have no professionals", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val profFromDb = db.getProfessionalById(id.toInt())
            if (profFromDb == null) {
                call.respondText("We have no users", status = HttpStatusCode.OK)
            } else {
                call.respond(profFromDb)
            }
        }
        // endregion

        // region post
        /**
         * POST a JSON representation of user object to be stored in `database`
         */
        post {
            val professional: Professional = call.receive()
            db.addProfessional(professional)
            call.respondText(
                "User Stored in `database`", status = HttpStatusCode.Created
            )
        }
        // endregion


        put {
            val professional: Professional = call.receive()
            db.updateProfessional(professional)
            call.respondText(
                "User updated in `database`", status = HttpStatusCode.Created
            )
        }

        // region delete
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (db.removeProfessional(id.toInt())) {
                call.respondText("User removed from `database", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
        // endregion
    }
}