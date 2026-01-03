package com.shadowconnect.routes

import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.db.DocumentsRepositoryImpl
import com.shadowconnect.shared.model.DocumentCreatedResponse
import com.shadowconnect.shared.model.DocumentCreatedResponse.DocumentCreatedDetails
import com.shadowconnect.shared.model.FieldPlacement
import com.shadowconnect.utils.logger
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

fun Route.documentRouting() {
    val database = DatabaseFactory.getDatabase()
    val professionalDocumentRepository = DocumentsRepositoryImpl(database)

    val DOCUMENSO_API_KEY = System.getenv("DOCUMENSO_API_KEY") ?: ""
    // upload document w/ field markings to documenso
    // save metadata to db
    // send webhook notifications
    route("/api/v1/documents") {
        authenticate("session-auth") {
            post("/create") {
                val multipart = call.receiveMultipart()
                var file: ByteArray? = null
                var title: String? = null
                var fields: String? = null


                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            file = part.streamProvider().readBytes()
                        }

                        is PartData.FormItem -> {
                            when (part.name) {
                                "title" -> title = part.value
                                "fields" -> fields = part.value
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }



                val fieldsList = fields?.let {
                    Json.decodeFromString<List<FieldPlacement>>(it)
                } ?: emptyList()

                // Build payload JSON
                val payloadMap = mapOf(
                    "title" to (title ?: "Untitled")
                )
                val payloadJson = Json.encodeToString(payloadMap)

                // Create OkHttp client and request
                // todo - single shared instance?
                val client = OkHttpClient()

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("payload", payloadJson)
                    .addFormDataPart(
                        "file",
                        "document.pdf",
                        file!!.toRequestBody("application/pdf".toMediaType())
                    )
                    .build()

                val request = Request.Builder()
                    .url("http://localhost:3000/api/v2/document/create")
                    .post(requestBody)
                    .addHeader("Authorization", DOCUMENSO_API_KEY)
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val documentResponse = DocumentCreatedResponse(true, DocumentCreatedDetails.fromJson(responseBody!!));
                    logger.debug("Documenso success response: ${response.code} - $responseBody")
                    // save needed parts of response to DB
                    try {
                        // todo - may need to include necessary data in internal request body, in order to pass it along in this persistence flow
                        val documentRecordId = professionalDocumentRepository.createDocumentEntry(
                            ownerId = 2L,
                            externalDocId = documentResponse.data.id,
                            envelopeId = documentResponse.data.envelopeId,
                            documentTitle = "title needed",
                            documentStatus = "new"
                        )
                        if (documentRecordId != null) {
                            logger.info("DOCUMENT RECORD CREATED! $documentRecordId")
                        }
                    } catch (e: Exception) {
                        logger.error("Error while creating internal document record", e)
                    }
                    // todo - send webhook notifications (for what exactly?) What is next step?
                    call.respond(HttpStatusCode.OK, documentResponse)
                } else {
                    val errorBody = response.body?.string()
                    logger.error("Error while creating document ${response.code} - $errorBody")
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("error" to errorBody)
                    )
                }
            }
        }
    }
}