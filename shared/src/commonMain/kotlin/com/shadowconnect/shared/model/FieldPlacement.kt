@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@Serializable
enum class FieldType(val displayName: String) {
    SIGNATURE("Signature"),
    DATE("Date"),
    TEXT("Text"),
}

/**
 * x,y - top left origin
 */
@Serializable
data class FieldPlacement(
    val id: String,
    val type: FieldType,
    val page: Int,
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double
) {
    companion object {
        @JsName("fromJson")
        fun fromJson(json: String): FieldPlacement {
            return Json.decodeFromString<FieldPlacement>(json)
        }

        @JsName("toJsonArray")
        fun toJsonArray(fields: Array<FieldPlacement>): String {
            return Json.encodeToString(ListSerializer(serializer()), fields.toList())
        }
    }
}
