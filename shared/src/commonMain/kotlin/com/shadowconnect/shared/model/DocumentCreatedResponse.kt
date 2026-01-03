@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Documenso integration API response
 */
@Serializable
data class DocumentCreatedResponse(
    val success: Boolean,
    val data: DocumentCreatedDetails
) {

    @Serializable
    data class DocumentCreatedDetails(
        val envelopeId: String,
        val id: Int
    ) {
        companion object {
        @JsName("fromJson")
        fun fromJson(jsonString: String): DocumentCreatedDetails {
            return Json.decodeFromString(serializer(), jsonString)
        }
    }
    }

    companion object {
        @JsName("fromJson")
        fun fromJson(jsonString: String): DocumentCreatedResponse {
            return Json.decodeFromString(serializer(), jsonString)
        }
    }
}