@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@Serializable
data class PhotoUploadResponse(
    val success: Boolean,
    val photoUrl: String? = null,
    val error: String? = null
)