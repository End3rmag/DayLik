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
    val priority: Priority
)

enum class Priority { LOW, MEDIUM, HIGH }

fun LocalDate.formatAsString(): String =
    "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}"

fun LocalDate.formatAsTimeString(): String {
    return "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}"
}
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.Companion.fromEpochDays(days: Int): LocalDate {
    return java.time.LocalDate.ofEpochDay(days.toLong()).toKotlinLocalDate()
}