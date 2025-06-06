package com.example.myapplication.ui.screen.component.Dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.ui.data.remote.Tasks.Priority
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.theme.MatuleTheme

@Composable
fun ChangeTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var priority by remember { mutableStateOf(task.priority) }
    var time by remember { mutableStateOf("") }
    var notifyEnabled by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.width(400.dp),
        title = { Text("Редактировать задачу") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .heightIn(max = 150.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .heightIn(max = 150.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { notifyEnabled = !notifyEnabled }
                ) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = { newValue ->
                            time = newValue.filter { it.isDigit() }.take(4)
                        },
                        label = { Text("00:00") },
                        modifier = Modifier.width(100.dp),
                        placeholder = { Text("00:00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = TimeTransformation()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Checkbox(
                        checked = notifyEnabled,
                        onCheckedChange = { notifyEnabled = it }
                    )
                    Text("Уведомить")
                }

                Text("Приоритет:", color = MatuleTheme.colors.dark_blue)

                Spacer(modifier = Modifier.height(16.dp))

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onDelete()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MatuleTheme.colors.bardovy,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Удалить")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val updatedTask = task.copy(
                            title = title,
                            description = description,
                            priority = priority,
                            time = time.takeIf { it.isNotBlank() },
                            notifyEnabled = notifyEnabled
                        )
                        onConfirm(updatedTask)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(MatuleTheme.colors.fox),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
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

