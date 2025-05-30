import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.MonthData
import com.example.myapplication.ui.data.remote.SimpleDate
import com.example.myapplication.ui.theme.MatuleTheme
import java.util.Calendar

@Preview
@Composable
fun dsa(){
    MatuleTheme{
        CalendarScreen()
    }
}

@Composable
fun CalendarScreen() {
    // Текущая дата (можно заменить на реальное получение даты)
    val today = remember {
        val calendar = Calendar.getInstance()
        SimpleDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val currentMonth = remember { mutableStateOf(today.toMonthData()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Заголовок с месяцем
        MonthHeader(
            monthData = currentMonth.value,
            onPrevious = { currentMonth.value = currentMonth.value.previous() },
            onNext = { currentMonth.value = currentMonth.value.next() }
        )

        // Дни недели
        WeekDaysHeader()

        // Дни месяца
        MonthGrid(
            monthData = currentMonth.value,
            currentDay = today,
            onDateSelected = { date -> /* Обработка выбора даты */ }
        )
    }
}

@Composable
fun MonthHeader(
    monthData: MonthData,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Default.ArrowBack, "Предыдущий месяц")
        }

        Text(
            text = "${getMonthName(monthData.month)} ${monthData.year}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MatuleTheme.typography.subTitleRegular16
        )

        IconButton(onClick = onNext) {
            Icon(Icons.Default.ArrowForward, "Следующий месяц")
        }
    }
}

@Composable
fun WeekDaysHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = MatuleTheme.colors.dark_blue
            )
        }
    }
}

@Composable
fun MonthGrid(
    monthData: MonthData,
    currentDay: SimpleDate,
    onDateSelected: (SimpleDate) -> Unit
) {
    val daysInMonth = monthData.daysInMonth()
    val firstDayOfWeek = monthData.getDayOfWeek(1) // 0-6 (Вс-Сб)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Пустые ячейки для выравнивания первого дня
        items(firstDayOfWeek) { }

        // Дни месяца
        items(daysInMonth) { day ->
            val date = SimpleDate(monthData.year, monthData.month, day + 1)
            val isToday = date == currentDay

            DayCell(
                day = day + 1,
                isCurrentDay = isToday,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    isCurrentDay: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .background(
                color = if (isCurrentDay) MatuleTheme.colors.fox else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = if (isCurrentDay) Color.White else MatuleTheme.colors.dark_blue
        )
    }
}

fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Январь"
        2 -> "Февраль"
        3 -> "Март"
        4 -> "Апрель"
        5 -> "Май"
        6 -> "Июнь"
        7 -> "Июль"
        8 -> "Август"
        9 -> "Сентябрь"
        10 -> "Октябрь"
        11 -> "Ноябрь"
        12 -> "Декабрь"
        else -> ""
    }
}