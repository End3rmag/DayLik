package com.example.myapplication.ui.data.remote

import kotlinx.datetime.LocalDate

data class SimpleDate(
    val year: Int,
    val month: Int, // 1-12
    val day: Int
) {
    fun formatAsString(): String = "${day.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}"

    fun toMonthData() = MonthData(year, month)
    fun toLocalDate(): LocalDate {
        return LocalDate(year, month, day)

    }

data class MonthData(
    val year: Int,
    val month: Int // 1-12
) {
    fun next(): MonthData {
        return if (month == 12) {
            MonthData(year + 1, 1)
        } else {
            MonthData(year, month + 1)
        }
    }

    fun previous(): MonthData {
        return if (month == 1) {
            MonthData(year - 1, 12)
        } else {
            MonthData(year, month - 1)
        }
    }

    fun daysInMonth(): Int {
        return when (month) {
            1 -> 31  // Январь
            2 -> if (year % 4 == 0) 29 else 28 // Февраль
            3 -> 31  // Март
            4 -> 30  // Апрель
            5 -> 31  // Май
            6 -> 30  // Июнь
            7 -> 31  // Июль
            8 -> 31  // Август
            9 -> 30  // Сентябрь
            10 -> 31 // Октябрь
            11 -> 30 // Ноябрь
            12 -> 31 // Декабрь
            else -> 30
        }
    }

    fun getDayOfWeek(day: Int): Int {
        // Простая реализация для определения дня недели (0-6, где 0 - воскресенье)
        val m = if (month < 3) month + 12 else month
        val y = if (month < 3) year - 1 else year
        return (day + (13 * (m + 1)) / 5 + y + y / 4 - y / 100 + y / 400) % 7
    }
}
}
