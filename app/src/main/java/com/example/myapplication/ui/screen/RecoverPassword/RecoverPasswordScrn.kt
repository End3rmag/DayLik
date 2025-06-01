package com.example.myapplication.ui.screen.RecoverPassword

import PasswordRecoveryDialog
import android.app.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.Otp
import com.example.myapplication.R
import com.example.myapplication.SignIn
import com.example.myapplication.ui.screen.Otp.OptScrn
import com.example.myapplication.ui.screen.component.AuthButton
import com.example.myapplication.ui.screen.component.AuthTextField
import com.example.myapplication.ui.screen.component.TitleWithSubtitleText
import com.example.myapplication.ui.theme.MatuleTheme


@Composable
fun RecoverPasswordScrn(navController: NavController) {
    val recoverPasswordViewModel: RecoverPasswordViewModel = viewModel()
    var isDialogOpen by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.systemBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .background(MatuleTheme.colors.biskuit)
                    .padding(top = 50.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            )

            {
                IconButton(onClick = {navController.navigate(route = SignIn)}) {
                    Icon(painter = painterResource(R.drawable.back_arrow),
                        contentDescription = null)
                }
            }
        },
    ) { paddingValues ->
        RecoverPasswordContent(paddingValues, recoverPasswordViewModel, onRequestOtp = { isDialogOpen = true })
    }

    if (isDialogOpen) {
        PasswordRecoveryDialog(
            isDialogOpen = isDialogOpen,
            onDismissAndNavigate = {
                isDialogOpen = false
                navController.navigate(route = Otp)
            }
        )
    }
}

@Composable
fun RecoverPasswordContent(
    paddingValues: PaddingValues,
    recoverPasswordViewModel: RecoverPasswordViewModel,
    onRequestOtp: () -> Unit
) {
    val recoverPasswordState = recoverPasswordViewModel.recoverPasswordState

    Column(
        modifier = Modifier
            .background(MatuleTheme.colors.biskuit)
            .padding(top = 100.dp)
            .fillMaxSize()
    ) {
        TitleWithSubtitleText(
            title = stringResource(R.string.miss_pass),
            subTitle = stringResource(R.string.enter_your_email)
        )

        Spacer(modifier = Modifier.height(35.dp))

        AuthTextField(
            value = recoverPasswordState.value.email,
            onChangeValue = { recoverPasswordViewModel.setEmail(it) },
            isError = recoverPasswordViewModel.emailHasError.value,
            placeholder = { Text(text = stringResource(R.string.template_email)) },
            supportingText = { Text(text = stringResource(R.string.incorrect_email)) },
            label = { Text(text = stringResource(R.string.email)) }
        )

        AuthButton(onClick = { onRequestOtp() }) {
            Text(stringResource(R.string.recover))
        }
    }
}