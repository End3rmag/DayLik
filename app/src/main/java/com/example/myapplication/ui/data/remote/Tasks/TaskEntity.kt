package com.example.myapplication.ui.data.remote.Tasks

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

// TaskEntity.kt
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val date: Long,
    val title: String,
    val description: String,
    val priority: Int
) {
    fun toTask(): Task {
        return Task(
            id = id,
            date = LocalDate.fromEpochDays(date.toInt()),
            title = title,
            description = description,
            priority = Priority.values()[priority]
        )
    }
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        date = date.toEpochDays().toLong(),
        title = title,
        description = description,
        priority = priority.ordinal
    )
}