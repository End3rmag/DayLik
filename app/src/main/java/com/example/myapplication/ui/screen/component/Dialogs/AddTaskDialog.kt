    package com.example.myapplication.ui.screen.component.Dialogs

    import android.util.Log
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
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
    import androidx.compose.material3.DropdownMenu
    import androidx.compose.material3.DropdownMenuItem
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
    import androidx.compose.ui.text.AnnotatedString
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.input.OffsetMapping
    import androidx.compose.ui.text.input.TransformedText
    import androidx.compose.ui.text.input.VisualTransformation
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.window.DialogProperties
    import androidx.room.util.TableInfo
    import com.example.myapplication.ui.data.remote.Tasks.Priority
    import com.example.myapplication.ui.data.remote.Tasks.RepeatType
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
        var priority by remember { mutableStateOf(Priority.LOW) }
        var time by remember { mutableStateOf("") }
        var notifyEnabled by remember { mutableStateOf(false) }
        var notifyDayBefore by remember { mutableStateOf(false) }
        var repeatType by remember { mutableStateOf(RepeatType.NONE) }
        var showRepeatMenu by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.width(350.dp),
            title = { Text("Добавить задачу на ${date.toString()}") },
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

                            Checkbox(
                                checked = notifyEnabled,
                                onCheckedChange = { notifyEnabled = it }
                            )
                            Text("Уведомить за час")
                        }


                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { notifyDayBefore = !notifyDayBefore }
                        ) {
                            Spacer(modifier = Modifier.width(100.dp))
                            Checkbox(
                                checked = notifyDayBefore,
                                onCheckedChange = { notifyDayBefore = it }
                            )
                            Text("Уведомить накануне (вечером)")
                        }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showRepeatMenu = true }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Повторение:",
                            color = MatuleTheme.colors.dark_blue,
                            modifier = Modifier.padding(end = 8.dp, start = 5.dp)
                        )

                        Text(
                            text = when (repeatType) {
                                RepeatType.NONE -> "Не повторять"
                                RepeatType.DAILY -> "Ежедневно"
                                RepeatType.WEEKLY -> "Еженедельно"
                                RepeatType.MONTHLY -> "Ежемесячно"
                                RepeatType.YEARLY -> "Ежегодно"
                            },
                            color = MatuleTheme.colors.dark_blue
                        )

                        DropdownMenu(
                            expanded = showRepeatMenu,
                            onDismissRequest = { showRepeatMenu = false }
                        ) {
                            RepeatType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            when (type) {
                                                RepeatType.NONE -> "Не повторять"
                                                RepeatType.DAILY -> "Ежедневно"
                                                RepeatType.WEEKLY -> "Еженедельно"
                                                RepeatType.MONTHLY -> "Ежемесячно"
                                                RepeatType.YEARLY -> "Ежегодно"
                                            }
                                        )
                                    },
                                    onClick = {
                                        repeatType = type
                                        showRepeatMenu = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Приоритет:", color = MatuleTheme.colors.dark_blue, modifier = Modifier.padding(start = 5.dp))

                    Spacer(modifier = Modifier.height(8.dp))

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
                            priority = priority,
                            time = time,
                            notifyEnabled = notifyEnabled,
                            notifyDayBefore = notifyDayBefore,
                            repeatType = repeatType,
                            originalTaskId = null,
                            
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

    class TimeTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val input = text.text
            val output = buildString {
                for (i in input.indices) {
                    append(input[i])
                    if (i == 1 && input.length > 2) append(":")
                }
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 2 -> offset
                        else -> offset + 1
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 2 -> offset
                        else -> offset - 1
                    }
                }
            }

            return TransformedText(AnnotatedString(output), offsetMapping)
        }
    }