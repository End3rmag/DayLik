// TasksViewModel.kt
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.data.remote.SimpleDate
import com.example.myapplication.ui.data.remote.Tasks.DayTask
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.data.remote.Tasks.TaskRepository
import com.example.myapplication.ui.data.remote.Tasks.toEntity
import com.example.myapplication.ui.data.remote.Tasks.toSimpleDate
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.util.UUID

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _tasks = mutableStateMapOf<SimpleDate, List<Task>>()
    val tasks: Map<SimpleDate, List<Task>> get() = _tasks

    private val _showDialog = mutableStateOf(false)
    val showDialog: Boolean get() = _showDialog.value

    private val _selectedDate = mutableStateOf<LocalDate?>(null)
    val selectedDate: LocalDate? get() = _selectedDate.value

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val taskEntities = repository.getTasks()
            _tasks.clear()

            taskEntities.forEach { entity ->
                val task = entity.toTask()
                val date = task.date.toSimpleDate()
                val currentTasks = _tasks[date] ?: emptyList()
                _tasks[date] = currentTasks + task
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insert(task.toEntity())
            val date = task.date.toSimpleDate()
            val currentTasks = _tasks[date] ?: emptyList()
            _tasks[date] = currentTasks + task
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task.toEntity())
            val date = task.date.toSimpleDate()
            _tasks[date] = _tasks[date]?.filter { it.id != task.id }!!
        }
    }

    fun showAddDialog(date: LocalDate) {
        _selectedDate.value = date
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun getTodayTasks(): List<Task> {
        val today = LocalDate.fromEpochDays(0) // Замените на текущую дату
        return _tasks[today.toSimpleDate()] ?: emptyList()
    }

    fun getUpcomingTasks(): List<DayTask> {
        return _tasks.map { (date, tasks) -> DayTask(date, tasks) }
            .sortedBy { it.date.toLocalDate().toEpochDays() }
    }
    fun addNewTask(title: String, description: String, priority: Priority, date: LocalDate) {
        viewModelScope.launch {
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                priority = priority,
                date = date
            )
            repository.insert(newTask.toEntity())
            loadTasks() // Перезагружаем задачи после добавления
        }
    }
}
