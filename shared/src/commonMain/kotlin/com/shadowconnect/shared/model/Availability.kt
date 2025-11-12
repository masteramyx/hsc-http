@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@Serializable
enum class DayOfWeek(val displayName: String) {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");
}

@Serializable
enum class TimeRange(val displayName: String, val startHour: Int, val endHour: Int) {
    MORNING("Morning (6am-12pm)", 6, 12),
    AFTERNOON("Afternoon (12pm-6pm)", 12, 18),
    EVENING("Evening (6pm-12am)", 18, 24);  // 24 as exclusive end

    fun contains(hour: Int): Boolean {
        return hour >= startHour && hour < endHour
    }
}