package com.example.myapplication.ui.screen.component.Dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.data.remote.Tasks.RepeatType
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.theme.MatuleTheme

//@Composable
//internal fun DialogDeleteRepitTasks(
//    task: Task,
//    onDismiss: () -> Unit,
//    onDelete: (deleteAllRepeats: Boolean) -> Unit
//) {
//    var deleteAll by remember { mutableStateOf(false) }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Удаление задачи") },
//        text = {
//            Column {
//                Text("Вы хотите удалить эту задачу?")
//
//                if (task.repeatType != RepeatType.NONE || task.originalTaskId != null) {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.clickable { deleteAll = !deleteAll }
//                    ) {
//                        Checkbox(
//                            checked = deleteAll,
//                            onCheckedChange = { deleteAll = it }
//                        )
//                        Text("Удалить все повторения этой задачи")
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = { onDelete(deleteAll) },
//                colors = ButtonDefaults.buttonColors(MatuleTheme.colors.bardovy)
//            ) {
//                Text("Удалить")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Отмена")
//            }
//        }
//    )
//}