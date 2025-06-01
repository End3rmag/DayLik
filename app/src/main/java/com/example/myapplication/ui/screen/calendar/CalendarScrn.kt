import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.SimpleDate
import com.example.myapplication.ui.data.remote.SimpleDate.MonthData
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.data.remote.Tasks.toSimpleDate
import com.example.myapplication.ui.screen.component.Dialogs.AddTaskDialog
import com.example.myapplication.ui.theme.MatuleTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar

@Composable
fun CalendarScrn(
    onBack: () -> Unit,
    tasksViewModel: TasksViewModel
) {
    // Получаем текущую дату
    val today = remember {
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toSimpleDate()
    }

    // Состояние текущего месяца
    val currentMonth = remember { mutableStateOf(today.toMonthData()) }

    // Используем tasks из ViewModel
    val tasks by remember { derivedStateOf { tasksViewModel.tasks } }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MatuleTheme.colors.biskuit
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            // Кнопка назад
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            // Календарь
            MonthHeader(
                monthData = currentMonth.value,
                onPrevious = { currentMonth.value = currentMonth.value.previous() },
                onNext = { currentMonth.value = currentMonth.value.next() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            WeekDaysHeader()

            MonthGrid(
                monthData = currentMonth.value,
                currentDay = today,
                tasks = tasks,
                onDateSelected = { date ->
                    tasksViewModel.showAddDialog(date.toLocalDate())
                }
            )
        }

        // Диалог добавления задачи
        if (tasksViewModel.showDialog) {
            tasksViewModel.selectedDate?.let { date ->
                AddTaskDialog(
                    date = date,
                    onDismiss = { tasksViewModel.dismissDialog() },
                    onConfirm = { task ->
                        tasksViewModel.addTask(task)
                    }
                )
            }
        }
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
    tasks: Map<SimpleDate, List<Task>>,
    onDateSelected: (SimpleDate) -> Unit
) {
    val daysInMonth = monthData.daysInMonth()
    val firstDayOfWeek = monthData.getDayOfWeek(1)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(firstDayOfWeek) { }

        items(daysInMonth) { day ->
            val date = SimpleDate(monthData.year, monthData.month, day + 1)
            val isToday = date == currentDay
            val hasTasks = tasks[date]?.isNotEmpty() ?: false

            DayCell(
                day = day + 1,
                isCurrentDay = isToday,
                hasTasks = hasTasks,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    isCurrentDay: Boolean,
    hasTasks: Boolean, // Новый параметр
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .then(
                if (isCurrentDay) {
                    Modifier.border(
                        width = 1.dp,
                        color = MatuleTheme.colors.fox,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
    ) {
        Text(
            text = day.toString(),
            color = if (isCurrentDay) MatuleTheme.colors.fox else MatuleTheme.colors.dark_blue,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Точка, если есть задачи
        if (hasTasks) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(4.dp)
                    .background(
                        color = MatuleTheme.colors.dark_blue,
                        shape = CircleShape
                    )
            )
        }
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