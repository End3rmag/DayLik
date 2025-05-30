package com.example.myapplication.ui.screen.SignIn

import android.media.SubtitleData
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset.Companion.Unspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.RecoverPassword
import com.example.myapplication.Registration
import com.example.myapplication.ui.screen.SignUp.SignUpScrn
import com.example.myapplication.ui.screen.component.AuthButton
import com.example.myapplication.ui.screen.component.AuthTextField
import com.example.myapplication.ui.screen.component.PasswordTextField
import com.example.myapplication.ui.screen.component.TitleWithSubtitleText
import com.example.myapplication.ui.theme.MatuleTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignInScrn(onSignInSuccess: () -> Unit,navController: NavHostController) {
    val signInViewModel: SignInViewModel = koinViewModel()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.background(MatuleTheme.colors.biskuit),
        snackbarHost = { SnackbarHost(snackBarHostState) },
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
                        contentDescription = null)
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
                    onClick = { navController.navigate(Registration) },

                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    elevation = null

                ) {
                    Text(
                        text = stringResource(R.string.sign_up),
                        style = MatuleTheme.typography.subTitleRegular16.copy(color = MatuleTheme.colors.text)
                    ) }
            }
        }
    ) { paddingValues ->
        SignInContent(paddingValues, signInViewModel, navController
        )
        val authorizationScreenState = signInViewModel.signInState
        LaunchedEffect(authorizationScreenState.value.isSignIn) {
            if(authorizationScreenState.value.isSignIn){
                onSignInSuccess()
            }
        }
        LaunchedEffect(authorizationScreenState.value.errorMessage) {
            authorizationScreenState.value.errorMessage?.let {
                snackBarHostState.showSnackbar(it)
            }
        }
    }
}

@Composable
fun SignInContent(paddingValues: PaddingValues, signInViewModel: SignInViewModel, navController: NavHostController) {
    val signInState = signInViewModel.signInState
    Column(
        modifier = Modifier.padding(paddingValues = paddingValues).fillMaxSize().background(MatuleTheme.colors.biskuit),
        verticalArrangement = Arrangement.spacedBy(16.dp),

    ) {
        TitleWithSubtitleText(
            title = stringResource(R.string.hello),
            subTitle = stringResource(R.string.sign_in_subtitle)
        )
        Spacer(modifier = Modifier.height(35.dp))

        AuthTextField(
            value = signInState.value.email,
            onChangeValue = { signInViewModel.setEmail(it) },
            isError = signInViewModel.emailHasError.value,
            placeholder = { Text(text = stringResource(R.string.template_email)) },
            supportingText = { Text(text = stringResource(R.string.incorrect_email)) },
            label = { Text(text = stringResource(R.string.email)) }
        )
        PasswordTextField(
            value = signInState.value.password,
            onValueChange = { signInViewModel.setPassword(it) },
            placeHolderText = stringResource(R.string.star_password),
            labelText = stringResource(R.string.password)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MatuleTheme.colors.biskuit)
        ) {
            Button(
                onClick = { navController.navigate(RecoverPassword) },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = null

            ) {
                Text(
                    text = "Восстановить",
                    style = MatuleTheme.typography.subTitleRegular16.copy(color = MatuleTheme.colors.text)
                ) }
        }

        AuthButton(
            onClick = {},

        ) {
            Text(stringResource(R.string.enter))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SignInScrnPreview() {
    // Создали экземпляр ViewModel (можно использовать mock или версию с предустановленными данными)
    val signInViewModel = SignInViewModel() // Нужно настроить правильную реализацию
    val navController = rememberNavController()

    SignInScrn(
        onSignInSuccess = { /* Действие при успешной авторизации */ },
        navController = navController
    )

    // Если потребуется, можно сделать и настройки, например:
    navController.navigate("someRoute")
}