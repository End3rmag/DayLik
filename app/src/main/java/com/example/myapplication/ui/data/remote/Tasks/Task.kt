package com.example.myapplication.ui.data.remote.Tasks

import kotlinx.datetime.LocalDate

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


fun String.formatAsTime(): String {
    return when {
        isEmpty() -> ""
        length <= 2 -> this
        else -> "${take(2)}:${drop(2).take(2)}"
    }
}