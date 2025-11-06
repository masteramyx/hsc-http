package com.shadowconnect.service

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.net.url.Url
import java.util.UUID

class R2StorageService {
    private val accountId = System.getenv("R2_ACCOUNT_ID")
        ?: throw IllegalStateException("R2_ACCOUNT_ID environment variable not set")

    private val accessKeyId = System.getenv("R2_PHOTOS_ACCESS_KEY_ID")
        ?: throw IllegalStateException("R2_PHOTOS_ACCESS_KEY_ID environment variable not set")

    private val secretAccessKey = System.getenv("R2_PHOTOS_SECRET_ACCESS_KEY")
        ?: throw IllegalStateException("R2_PHOTOS_SECRET_ACCESS_KEY environment variable not set")

    private val bucketName = System.getenv("R2_PHOTOS_BUCKET_NAME")
        ?: throw IllegalStateException("R2_PHOTOS_BUCKET_NAME environment variable not set")

    private val publicUrl = System.getenv("R2_PHOTOS_PUBLIC_URL")
        ?: throw IllegalStateException("R2_PHOTOS_PUBLIC_URL environment variable not set")

    private val s3Client = S3Client {
        region = "auto" // R2 uses "auto" region
        endpointUrl = Url.parse("https://$accountId.r2.cloudflarestorage.com")
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = this@R2StorageService.accessKeyId
            secretAccessKey = this@R2StorageService.secretAccessKey
        }
    }

    /**
     * Uploads a file to R2 and returns the public URL
     *
     * @param bytes File content as byte array
     * @param contentType MIME type (e.g., "image/jpeg", "image/png")
     * @param extension File extension (e.g., "jpg", "png")
     * @return Public URL of the uploaded file
     */
    suspend fun uploadFile(bytes: ByteArray, contentType: String, extension: String): String {
        val filename = "${UUID.randomUUID()}.$extension"

        val request = PutObjectRequest {
            bucket = bucketName
            key = filename
            body = ByteStream.fromBytes(bytes)
            this.contentType = contentType
        }

        s3Client.putObject(request)

        return "$publicUrl/$filename"
    }

    /**
     * Deletes a file from R2
     *
     * @param filename The filename to delete (not the full URL)
     */
    suspend fun deleteFile(filename: String) {
        val request = DeleteObjectRequest {
            bucket = bucketName
            key = filename
        }

        s3Client.deleteObject(request)
    }

    /**
     * Extracts filename from a full R2 public URL
     *
     * @param url Full public URL
     * @return Filename only
     */
    fun extractFilename(url: String): String {
        return url.substringAfterLast('/')
    }

    /**
     * Closes the S3 client
     */
    fun close() {
        s3Client.close()
    }
}
