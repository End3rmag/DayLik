package com.example.myapplication.ui.screen.component.TasksItems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.data.remote.Tasks.formatAsTime
import com.example.myapplication.ui.theme.MatuleTheme

@Composable
fun UpcomingTaskItem(task: Task,
                     onDelete: () -> Unit = {},
                     onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick=onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = when (task.priority) {
                        Priority.HIGH -> MatuleTheme.colors.bardovy
                        Priority.MEDIUM -> MatuleTheme.colors.fox
                        Priority.LOW -> MatuleTheme.colors.darkgreen
                    },
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MatuleTheme.colors.dark_blue,
            modifier = Modifier.weight(1f)
        )

        if (task.time != null) {
            Text(
                text = task.time.formatAsTime(),
                style = MaterialTheme.typography.labelSmall,
                color = MatuleTheme.colors.dark_blue.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

