package com.example.myapplication.ui.data.remote.Tasks

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

data class Task(
    val id: String,
    val date: LocalDate,
    val title: String,
    val description: String,
    val priority: Priority,
    val time: String? = null,
    val notifyEnabled: Boolean = false
)

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

fun LocalDate.formatAsString(): String =
    "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}"

fun LocalDate.formatAsTimeString(): String {
    return "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}"
}
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.Companion.fromEpochDays(days: Int): LocalDate {
    return java.time.LocalDate.ofEpochDay(days.toLong()).toKotlinLocalDate()
}
fun String.formatAsTime(): String {
    return when {
        isEmpty() -> ""
        length <= 2 -> this
        else -> "${take(2)}:${drop(2).take(2)}"
    }
}