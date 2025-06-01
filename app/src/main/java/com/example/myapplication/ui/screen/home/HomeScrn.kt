import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.Tasks.DayTask
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.screen.component.TasksItems.TaskItem
import com.example.myapplication.ui.screen.component.TasksItems.UpcomingTaskItem
import com.example.myapplication.ui.theme.MatuleTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScrn(
    onNavigateToCalendar: () -> Unit = {},
    tasksViewModel: TasksViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    // Получаем текущую дату
    val today = remember {
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    val todayTasks by remember { derivedStateOf { tasksViewModel.getTodayTasks() } }
    val upcomingTasks by remember { derivedStateOf { tasksViewModel.getUpcomingTasks() } }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MatuleTheme.colors.biskuit)
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            // Шапка с кнопками
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Задачи на сегодня",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                Row {
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Календарь")
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить задачу")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список задач на сегодня
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = todayTasks.size,
                    itemContent = { index ->
                        TaskItem(
                            task = todayTasks[index],
                            onDelete = { tasksViewModel.deleteTask(todayTasks[index]) }
                        )
                    }
                )

                // Если задач нет, показываем сообщение
                if (todayTasks.isEmpty()) {
                    item {
                        Text(
                            text = "Нет задач на сегодня",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Ближайшие задачи
            if (upcomingTasks.isNotEmpty()) {
                Text(
                    text = "Ближайшие задачи",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                upcomingTasks.take(3).forEach { dayTask ->
                    Column {
                        Text(
                            text = dayTask.date.formatAsString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        dayTask.tasks.forEach { task ->
                            UpcomingTaskItem(
                                task = task,
                                onDelete = { tasksViewModel.deleteTask(task) }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Floating Action Button для добавления задач
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MatuleTheme.colors.fox
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }

    // Диалог добавления задачи
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Новая задача") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text("Название") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        label = { Text("Описание") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Приоритет:", color = MatuleTheme.colors.dark_blue)

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Priority.values().forEach { priority ->
                            PriorityChip(
                                priority = priority,
                                isSelected = priority == selectedPriority,
                                onSelected = { selectedPriority = priority }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        tasksViewModel.addNewTask(
                            title = newTaskTitle,
                            description = newTaskDescription,
                            priority = selectedPriority,
                            date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                        )
                        showAddDialog = false
                        newTaskTitle = ""
                        newTaskDescription = ""
                    },
                    colors = ButtonDefaults.buttonColors(MatuleTheme.colors.fox)
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Отмена", color = MatuleTheme.colors.dark_blue)
                }
            }
        )
    }
}

@Composable
private fun PriorityChip(
    priority: Priority,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (priority) {
        Priority.LOW -> Color.Green
        Priority.MEDIUM -> MatuleTheme.colors.fox
        Priority.HIGH -> Color.Red
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) priorityColor.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(1.dp, priorityColor)
    ) {
        Text(
            text = when (priority) {
                Priority.LOW -> "Низкий"
                Priority.MEDIUM -> "Средний"
                Priority.HIGH -> "Высокий"
            },
            color = MatuleTheme.colors.dark_blue,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable(onClick = onSelected)
        )
    }
}