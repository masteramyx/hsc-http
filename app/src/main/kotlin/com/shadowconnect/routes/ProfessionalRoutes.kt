package com.shadowconnect.routes

import com.shadowconnect.plugins.db
import io.ktor.http.*
import io.ktor.server.application.*
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
    }
}