package com.example.myapplication.ui.screen.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.common.CommonButton
import com.example.myapplication.ui.theme.MatuleTheme

@Composable
fun AuthButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    CommonButton(
        onClick = onClick,
        buttonColors = ButtonColors(
            contentColor = MatuleTheme.colors.background,
            containerColor = MatuleTheme.colors.fox,
            disabledContentColor = MatuleTheme.colors.fox,
            disabledContainerColor = MatuleTheme.colors.fox
        ),
        modifier = modifier.padding(top = 50.dp),
    ){
        content()
    }
}