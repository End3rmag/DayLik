package com.example.myapplication.ui.screen.component.Dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.theme.MatuleTheme
import kotlinx.datetime.LocalDate
import java.util.UUID

@Composable
fun AddTaskDialog(
    date: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить задачу на ${date.toString()}") },
        text = {
            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Приоритет:", color = MatuleTheme.colors.dark_blue)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Priority.values().forEach { p ->
                        PriorityChip(
                            priority = p,
                            isSelected = p == priority,
                            onSelected = { priority = p }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newTask = Task(
                        id = UUID.randomUUID().mostSignificantBits.toString(),
                        date = date,
                        title = title,
                        description = description,
                        priority = priority
                    )
                    onConfirm(newTask)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(MatuleTheme.colors.fox)
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = MatuleTheme.colors.dark_blue)
            }
        }
    )
}

@Composable
private fun PriorityChip(
    priority: Priority,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (priority) {
        Priority.LOW -> MatuleTheme.colors.darkgreen
        Priority.MEDIUM -> MatuleTheme.colors.fox
        Priority.HIGH -> MatuleTheme.colors.bardovy
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