package com.shadowconnect.routes

import com.shadowconnect.model.Organization
import com.shadowconnect.model.Student
import com.shadowconnect.plugins.db
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.organizationRoutes() {

    route("/organizations") {

        get {
            val orgsFromDb = db.getOrganizations()
            if (orgsFromDb.isNotEmpty()) {
                call.respond(orgsFromDb)
            } else {
                call.respondText("We have no organizations", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val orgFromDb = db.getOrganizationById(id.toInt())
            if (orgFromDb == null) {
                call.respondText("We have no users", status = HttpStatusCode.OK)
            } else {
                call.respond(orgFromDb)
            }
        }

        /**
         * POST a JSON representation of user object to be stored in `database`
         */
        post {
            val organization: Organization = call.receive()
            db.addOrganization(organization)
            call.respondText(
                "Organization Stored in `database`", status = HttpStatusCode.Created
            )
        }

        put {
            val organization: Organization = call.receive()
            db.updateOrganization(organization)
            call.respondText(
                "Organization updated in `database`", status = HttpStatusCode.Created
            )
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (db.removeOrganization(id.toInt())) {
                call.respondText("Org removed from `database", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Org not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}