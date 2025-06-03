package com.example.myapplication.ui.data.remote

import kotlinx.datetime.LocalDate

data class SimpleDate(
    val year: Int,
    val month: Int, // 1-12
    val day: Int
) {
    fun formatAsString(): String =
        "${day.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}"

    fun toMonthData() = MonthData(year, month)

    fun toLocalDate(): LocalDate = LocalDate(year, month, day)
}

data class MonthData(
    val year: Int,
    val month: Int // 1-12
) {
    companion object {
        const val MONDAY = 0
        const val TUESDAY = 1
        const val WEDNESDAY = 2
        const val THURSDAY = 3
        const val FRIDAY = 4
        const val SATURDAY = 5
        const val SUNDAY = 6

        private val monthNames = listOf(
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        )

        fun getMonthName(month: Int): String = monthNames[month - 1]
    }

    fun next(): MonthData = if (month == 12)
        MonthData(year + 1, 1) else MonthData(year, month + 1)

    fun previous(): MonthData = if (month == 1)
        MonthData(year - 1, 12) else MonthData(year, month - 1)

    fun daysInMonth(): Int = when (month) {
        1 -> 31   // Январь
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        3 -> 31   // Март
        4 -> 30   // Апрель
        5 -> 31   // Май
        6 -> 30   // Июнь
        7 -> 31   // Июль
        8 -> 31   // Август
        9 -> 30   // Сентябрь
        10 -> 31  // Октябрь
        11 -> 30  // Ноябрь
        12 -> 31  // Декабрь
        else -> 30
    }

    fun getDayOfWeek(day: Int): Int {
        // Алгоритм Зеллера (0=ПН, 1=ВТ, 2=СР, 3=ЧТ, 4=ПТ, 5=СБ, 6=ВС)
        var m = month
        var y = year

        if (m < 3) {
            m += 12
            y -= 1
        }

        val k = y % 100
        val j = y / 100

        // Формула возвращает (0=СБ, 1=ВС, 2=ПН,...,6=ПТ)
        var h = (day + (13*(m+1))/5 + k + k/4 + j/4 + 5*j) % 7

        // Преобразуем к (0=ПН, 1=ВТ,...,6=ВС)
        return (h + 5) % 7
    }

    fun getMonthName(): String = getMonthName(month)
}
