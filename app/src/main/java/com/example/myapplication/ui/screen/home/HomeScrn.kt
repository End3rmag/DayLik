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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Divider
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
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.screen.component.Dialogs.AddTaskDialog
import com.example.myapplication.ui.screen.component.Dialogs.ChangeTaskDialog
import com.example.myapplication.ui.screen.component.TasksItems.TaskItem
import com.example.myapplication.ui.screen.component.TasksItems.UpcomingTaskItem
import com.example.myapplication.ui.theme.MatuleTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScrn(
    onNavigateToCalendar: () -> Unit = {},
    tasksViewModel: TasksViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val todayTasks by remember { derivedStateOf { tasksViewModel.getTodayTasks() } }
    val upcomingTasks by remember { derivedStateOf { tasksViewModel.getUpcomingTasks() } }
    var selectedTask by remember { mutableStateOf<Task?>(null)}

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MatuleTheme.colors.biskuit)
                .systemBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Задачи на сегодня",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                Row{
                    IconButton(onClick = onNavigateToCalendar)  {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Календарь")
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = todayTasks.size,
                    itemContent = { index ->
                        TaskItem(
                            task = todayTasks[index],
                            onDelete = { tasksViewModel.deleteTask(todayTasks[index]) },
                            onClick = {
                                selectedTask = todayTasks[index]
                                showEditDialog = true
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                )


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

            if (upcomingTasks.isNotEmpty()) {
                Divider(
                    color = MatuleTheme.colors.dark_blue.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
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
                                onDelete = { tasksViewModel.deleteTask(task) },
                                onClick = {
                                    selectedTask = task
                                    showEditDialog = true
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding(),
            containerColor = MatuleTheme.colors.fox,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }

    if (showAddDialog) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        AddTaskDialog(
            date = today,
            onDismiss = { showAddDialog = false },
            onConfirm = { newTask ->
                tasksViewModel.addNewTask(
                    title = newTask.title,
                    description = newTask.description,
                    priority = newTask.priority,
                    date = newTask.date,
                    time = newTask.time,
                    notifyEnabled = newTask.notifyEnabled
                )
            }
        )
    }

    selectedTask?.let { task ->
        if (showEditDialog) {
            ChangeTaskDialog(
                task = task,
                onDismiss = { showEditDialog = false },
                onConfirm = { updatedTask ->
                    tasksViewModel.updateTask(updatedTask)
                    showEditDialog = false
                },
                onDelete = {
                    tasksViewModel.deleteTask(task)
                    showEditDialog = false
                }
            )
        }
    }
}