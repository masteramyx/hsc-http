package com.shadowconnect.routes

import com.shadowconnect.service.R2StorageService
import com.shadowconnect.shared.model.PhotoUploadResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.asStream
import io.ktor.utils.io.jvm.javaio.toInputStream
import io.ktor.utils.io.*

fun Route.fileUploadRouting() {
    val r2Service = R2StorageService()
    val maxFileSize = 5 * 1024 * 1024 // 5MB in bytes
    val allowedContentTypes = setOf("image/jpeg", "image/jpg", "image/png", "image/webp")
    val allowedExtensions = mapOf(
        "image/jpeg" to "jpg",
        "image/jpg" to "jpg",
        "image/png" to "png",
        "image/webp" to "webp"
    )

    post("/api/upload/photo") {
        try {
            val multipart = call.receiveMultipart()
            var photoBytes: ByteArray? = null
            var contentType: String? = null
            var errorMessage: String? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val fileBytes = part.provider().asStream().readBytes()
                        val fileContentType = part.contentType?.toString()

                        // Validate file size
                        if (fileBytes.size > maxFileSize) {
                            errorMessage = "File size exceeds 5MB limit"
                            part.dispose()
                            return@forEachPart
                        }

                        // Validate content type
                        if (fileContentType == null || fileContentType !in allowedContentTypes) {
                            errorMessage = "Invalid file type. Only JPG, PNG, and WebP are allowed"
                            part.dispose()
                            return@forEachPart
                        }

                        photoBytes = fileBytes
                        contentType = fileContentType
                    }
                    else -> {
                        // Ignore other part types
                    }
                }
                part.dispose()
            }

            // Check for errors from validation
            if (errorMessage != null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    PhotoUploadResponse(
                        success = false,
                        error = errorMessage
                    )
                )
                return@post
            }

            // Check if file was provided
            if (photoBytes == null || contentType == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    PhotoUploadResponse(
                        success = false,
                        error = "No file provided"
                    )
                )
                return@post
            }

            // Get file extension
            val extension = allowedExtensions[contentType]
                ?: throw IllegalStateException("Unmapped content type: $contentType")

            // Upload to R2
            val photoUrl = r2Service.uploadFile(photoBytes!!, contentType!!, extension)

            call.respond(
                HttpStatusCode.OK,
                PhotoUploadResponse(
                    success = true,
                    photoUrl = photoUrl
                )
            )

        } catch (e: Exception) {
            application.log.error("Error uploading photo", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                PhotoUploadResponse(
                    success = false,
                    error = "Failed to upload photo: ${e.message}"
                )
            )
        }
    }
}
