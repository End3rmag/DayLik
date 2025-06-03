package com.example.myapplication.ui.screen.component.TasksItems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.data.remote.Tasks.formatAsTimeString
import com.example.myapplication.ui.theme.MatuleTheme

@Composable
fun TaskItem(
    task: Task,
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {}, // Это параметр, который вы передаете
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Здесь нужно использовать переданный onClick
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Остальной код без изменений
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    color = when (task.priority) {
                        Priority.HIGH -> MatuleTheme.colors.bardovy
                        Priority.MEDIUM -> MatuleTheme.colors.fox
                        Priority.LOW -> MatuleTheme.colors.darkgreen
                    },
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (task.priority == Priority.HIGH) FontWeight.Bold else FontWeight.Normal
                ),
                color = MatuleTheme.colors.dark_blue
            )

            if (task.description.isNotBlank()) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MatuleTheme.colors.dark_blue.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        if (task.date != null) {
            Text(
                text = task.date.formatAsTimeString(),
                style = MaterialTheme.typography.labelSmall,
                color = MatuleTheme.colors.dark_blue.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}