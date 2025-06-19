package com.example.myapplication.ui.data.remote.Tasks

import kotlinx.datetime.LocalDate

data class Task(
    val id: String,
    val date: LocalDate,
    val title: String,
    val description: String,
    val priority: Priority,
    val time: String? = null,
    val notifyEnabled: Boolean = false,
    val notifyDayBefore: Boolean = false,
    val repeatType: RepeatType = RepeatType.NONE,
    val originalTaskId: String? = null,
    val nextOccurrence: LocalDate? = null
)

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

enum class RepeatType {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}


fun String.formatAsTime(): String {
    return when {
        isEmpty() -> ""
        length <= 2 -> this
        else -> "${take(2)}:${drop(2).take(2)}"
    }
}