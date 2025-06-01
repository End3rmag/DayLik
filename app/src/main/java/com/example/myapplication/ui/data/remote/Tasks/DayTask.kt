package com.example.myapplication.ui.data.remote.Tasks

import com.example.myapplication.ui.data.remote.SimpleDate
import kotlinx.datetime.LocalDate

data class DayTask(
    val date: SimpleDate,
    val tasks: List<Task> = emptyList()
)

fun LocalDate.toSimpleDate(): SimpleDate {
    return SimpleDate(
        day = this.dayOfMonth,
        month = this.monthNumber,
        year = this.year
    )
}
