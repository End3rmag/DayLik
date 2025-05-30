package com.example.myapplication.ui.data.remote

import kotlinx.datetime.LocalDate

data class Task(
    val id: String,
    val date: LocalDate,
    val title: String,
    val description: String,
    val priority: Priority
)

enum class Priority { LOW, MEDIUM, HIGH }