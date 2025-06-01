package com.example.myapplication.ui.screen.SignUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.SignIn
import com.example.myapplication.ui.screen.component.AuthButton
import com.example.myapplication.ui.screen.component.AuthTextField
import com.example.myapplication.ui.screen.component.PasswordTextField
import com.example.myapplication.ui.screen.component.TitleWithSubtitleText
import com.example.myapplication.ui.theme.MatuleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScrn(onNavigationToHome: () -> Unit, navController: NavController) {
    val signUpViewModel: SignUpViewModel = koinViewModel()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier.fillMaxSize().background(MatuleTheme.colors.biskuit).systemBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .background(MatuleTheme.colors.biskuit)
                    .padding(top = 35.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MatuleTheme.colors.biskuit)
                    .padding(bottom = 50.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Button(
                    onClick = { navController.navigate(SignIn) },

                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    elevation = null

                ) {
                    Text(
                        text = stringResource(R.string.sign_in),
                        style = MatuleTheme.typography.subTitleRegular16.copy(color = MatuleTheme.colors.text)
                    ) }
            }
        }
    )
    {
        paddingValues ->
        SignUpContent(paddingValues, signUpViewModel)

        val registrationScreenState = signUpViewModel.signUpState
        LaunchedEffect(registrationScreenState.value.isSignUp) {
            if(registrationScreenState.value.isSignUp) {
                onNavigationToHome()
            }
        }

        LaunchedEffect(registrationScreenState.value.errorMessage) {
            registrationScreenState.value.errorMessage?.let {
                snackBarHostState.showSnackbar(it)
            }
        }
    }
}

@Composable
fun SignUpContent(paddingValues: PaddingValues, signUpViewModel: SignUpViewModel) {
    val signUpState = signUpViewModel.signUpState

    Column(
        modifier = Modifier.fillMaxSize().background(MatuleTheme.colors.biskuit).padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleWithSubtitleText(
            title = stringResource(R.string.registration),
            subTitle = stringResource(R.string.sign_in_subtitle)
        )

        AuthTextField(
            value = signUpState.value.name,
            onChangeValue = { signUpViewModel.setName(it) },
            isError = false,
            placeholder = { Text(text = stringResource(R.string.enter_name)) },
            supportingText = {Text(text = stringResource(R.string.incorrect_name)) },
            label = { Text(text = stringResource(R.string.name)) }
        )

        AuthTextField(
            value = signUpState.value.email,
            onChangeValue = { signUpViewModel.setEmail(it) },
            isError = signUpViewModel.emailHasError.value,
            placeholder = { Text(text = stringResource(R.string.template_email)) },supportingText = { if (signUpViewModel.emailHasError.value) Text(text = stringResource(R.string.enter_email)) },
            label = { Text(text = stringResource(R.string.email)) }
        )

        PasswordTextField(
            value = signUpState.value.password,
            onValueChange = { signUpViewModel.setPassword(it) },
            placeHolderText = stringResource(R.string.star_password),
            labelText = stringResource(R.string.password)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(MatuleTheme.colors.biskuit).padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.policy_check),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(R.string.privacy_policy),
                modifier = Modifier.padding(start = 20.dp),
                style = MatuleTheme.typography.bodyRegular12.copy(
                    color = MatuleTheme.colors.subTextDark,
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        AuthButton(onClick = {
            signUpViewModel.signUp()
        }) {
            Text(stringResource(R.string.sign_up))
            if (signUpState.value.isLoading) CircularProgressIndicator(color = Color.White)
        }
    }
}