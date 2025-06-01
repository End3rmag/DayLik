import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.ui.data.remote.Tasks.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAll(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}

class TaskRepository(private val dao: TaskDao) {
    suspend fun getTasks(): List<TaskEntity> = dao.getAll()
    suspend fun insert(task: TaskEntity) = dao.insert(task)
    suspend fun delete(task: TaskEntity) = dao.delete(task)
}