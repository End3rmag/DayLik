package com.example.myapplication.ui.screen.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MatuleTheme


    @Composable
    fun PasswordTextField(
        value: String,
        onValueChange: (String) -> Unit,
        placeHolderText: String? = null,
        labelText: String? = null
    ) {
        var showPassword by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (labelText != null) {
                Text(
                    text = labelText,
                    style = MatuleTheme.typography.bodyRegular16.copy(MatuleTheme.colors.text),
                    textAlign = TextAlign.Start
                )
            }
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(end = 8.dp),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    if (placeHolderText != null) {
                        Text(
                            text = placeHolderText,
                            style = MatuleTheme.typography.bodyRegular14.copy(color = MatuleTheme.colors.hint)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = "Переключить видимость пароля"
                        )
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MatuleTheme.colors.background,
                    unfocusedContainerColor = MatuleTheme.colors.background,
                    disabledContainerColor = MatuleTheme.colors.background,
                    errorContainerColor = MatuleTheme.colors.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                isError = false
            )
        }
    }
