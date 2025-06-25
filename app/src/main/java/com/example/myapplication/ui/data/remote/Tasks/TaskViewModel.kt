// TasksViewModel.kt
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.data.local.repository.TaskRepository
import com.example.myapplication.ui.data.remote.SimpleDate
import com.example.myapplication.ui.data.remote.Tasks.DayTask
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.RepeatType
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.data.remote.Tasks.toEntity
import com.example.myapplication.ui.data.remote.Tasks.toSimpleDate
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _tasks = mutableStateMapOf<SimpleDate, List<Task>>()
    val tasks: Map<SimpleDate, List<Task>> get() = _tasks

    private val mutex = Mutex()

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
            if (task.repeatType == RepeatType.NONE) {
                // Обычная задача
                repository.insert(task.toEntity())
                updateTasksInState(task)
            } else {
                // Для повторяющейся задачи сохраняем оригинал и генерируем повторения
                val originalTask = if (task.originalTaskId == null) {
                    // Это новая повторяющаяся задача
                    task.copy(originalTaskId = task.id)
                } else {
                    // Это уже повторяющаяся задача
                    task
                }

                // Сохраняем оригинальную задачу
                repository.insert(originalTask.toEntity())
                updateTasksInState(originalTask)

                // Генерируем повторения
                generateAndSaveRepeatedTasks(originalTask)
            }
        }
    }

    private suspend fun generateAndSaveRepeatedTasks(originalTask: Task) {
        val currentYear = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.year

        // Удаляем старые повторения (если они есть)
        originalTask.originalTaskId?.let { originalId ->
            val oldRepeats = repository.getTasksByOriginalId(originalId)
                .filter { it.id != originalId } // Не удаляем оригинал
            oldRepeats.forEach {
                repository.delete(it)
                removeTaskFromState(it.toTask())
            }
        }

        // Генерируем новые повторения
        val repeatedTasks = generateRepeatedTasks(originalTask, currentYear)
        repeatedTasks.forEach {
            repository.insert(it.toEntity())
            updateTasksInState(it)
        }
    }

    private fun generateRepeatedTasks(originalTask: Task, year: Int): List<Task> {
        if (originalTask.repeatType == RepeatType.NONE) return emptyList()

        val tasks = mutableListOf<Task>()
        val originalId = originalTask.originalTaskId ?: originalTask.id

        var currentDate = originalTask.date
        val endDate = LocalDate(year, 12, 31)

        while (currentDate <= endDate) {
            // Пропускаем оригинальную дату (она уже сохранена)
            if (currentDate != originalTask.date) {
                tasks.add(originalTask.copy(
                    id = UUID.randomUUID().toString(),
                    date = currentDate,
                    originalTaskId = originalId
                ))
            }

            currentDate = when (originalTask.repeatType) {
                RepeatType.DAILY -> currentDate.plus(1, DateTimeUnit.DAY)
                RepeatType.WEEKLY -> currentDate.plus(1, DateTimeUnit.WEEK)
                RepeatType.MONTHLY -> currentDate.plus(1, DateTimeUnit.MONTH)
                RepeatType.YEARLY -> currentDate.plus(1, DateTimeUnit.YEAR)
                else -> break // Для RepeatType.NONE не должно сюда попасть
            }
        }

        return tasks
    }



    fun deleteTask(task: Task, deleteAllRepeats: Boolean = false) {
        viewModelScope.launch{
        mutex.withLock{
            try {
                if (deleteAllRepeats) {
                    // 1. Получаем ID для удаления
                    val originalId = task.originalTaskId ?: task.id

                    // 2. Удаляем из базы данных
                    repository.deleteAllByOriginalId(originalId)

                    // 3. Создаем новый Map без удаленных задач
                    val newTasksMap = _tasks
                        .mapValues { (_, tasksList) ->
                            tasksList.filterNot { it.originalTaskId == originalId || it.id == originalId }
                        }
                        .filterValues { it.isNotEmpty() }

                    // 4. Атомарно обновляем состояние
                    _tasks.clear()
                    _tasks.putAll(newTasksMap)
                } else {
                    // Простое удаление одной задачи
                    repository.delete(task.toEntity())

                    // Безопасное удаление из состояния
                    val date = task.date.toSimpleDate()
                    _tasks[date] = _tasks[date]?.filter { it.id != task.id } ?: emptyList()

                    // Удаляем дату, если задач больше нет
                    if (_tasks[date]?.isEmpty() == true) {
                        _tasks.remove(date)
                    }
                }
            } catch (e: Exception) { }
        }
    }
    }


    private fun updateTasksInState(task: Task) {
        val date = task.date.toSimpleDate()
        val currentTasks = _tasks[date] ?: emptyList()
        _tasks[date] = currentTasks + task
    }

    private fun removeTaskFromState(task: Task) {
        val date = task.date.toSimpleDate()
        _tasks[date] = _tasks[date]?.filter { it.id != task.id } ?: emptyList()

        // Удаляем дату, если задач больше нет
        if (_tasks[date]?.isEmpty() == true) {
            _tasks.remove(date)
        }
    }

    fun checkAndGenerateNewYearTasks() {
        viewModelScope.launch {
            val currentYear = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.year

            // Получаем все оригинальные повторяющиеся задачи
            val allTasks = repository.getTasks()
            val repeatingTasks = allTasks
                .map { it.toTask() }
                .filter { it.repeatType != RepeatType.NONE && (it.originalTaskId == null || it.originalTaskId == it.id) }

            repeatingTasks.forEach { originalTask ->
                // Проверяем, есть ли уже задачи на текущий год
                val hasTasksThisYear = allTasks.any {
                    it.toTask().date.year == currentYear &&
                            (it.originalTaskId == originalTask.id || it.id == originalTask.id)
                }

                if (!hasTasksThisYear) {
                    generateAndSaveRepeatedTasks(originalTask)
                }
            }
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
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val allTasks = _tasks.values.flatten()
        return allTasks
            .filter { it.date == today }
            .sortedBy { it.priority }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.update(updatedTask.toEntity())
            loadTasks()
        }
    }

    fun getUpcomingTasks(): List<DayTask> {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        return _tasks
            .filter { it.key.toLocalDate() > today }
            .map { (date, tasks) ->
                DayTask(
                    date,
                    tasks.sortedBy { it.priority }
                )
            }
            .sortedBy { it.date.toLocalDate().toEpochDays() }
    }
    fun addNewTask(title: String,
                   description: String,
                   priority: Priority,
                   date: LocalDate,
                   time: String? = null,
                   notifyEnabled: Boolean = false,
                   notifyDayBefore: Boolean = false ) {
        viewModelScope.launch {
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                priority = priority,
                date = date,
                time = time,
                notifyEnabled = notifyEnabled,
                notifyDayBefore = notifyDayBefore
            )
            Log.d("TASK_DEBUG", "Adding task to repository: $newTask")
            repository.insert(newTask.toEntity())
            loadTasks()
        }
    }
}
