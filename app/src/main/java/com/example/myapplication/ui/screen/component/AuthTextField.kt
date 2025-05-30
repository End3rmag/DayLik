package com.example.myapplication.ui.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.common.CommonTextField

@Composable
fun AuthTextField(
    value: String,
    onChangeValue: (String) -> Unit,
    isError: Boolean,
    supportingText: @Composable () -> Unit,
    placeholder: @Composable () -> Unit,
    label: @Composable () -> Unit
){
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        label()
        CommonTextField(
            value = value,
            onChangeValue = onChangeValue,
            isError =  isError,
            supportingText = supportingText,
            placeHolder = placeholder
        )
    }
}