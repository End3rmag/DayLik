package com.example.myapplication.ui.screen.SignUp

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.data.remote.User
import com.example.myapplication.ui.local.DataStoreOnBoarding
import com.example.myapplication.ui.local.LocalStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val localStorage: LocalStorage,
    private val dataStoreOnBoarding: DataStoreOnBoarding
) : ViewModel() {
    // Состояние формы
    var signUpState = mutableStateOf(SignUpState())
        private set

    // Локальное "хранилище" пользователей
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    // Валидация email
    val emailHasError = derivedStateOf {
        if (signUpState.value.email.isEmpty()) return@derivedStateOf false
        !android.util.Patterns.EMAIL_ADDRESS.matcher(signUpState.value.email).matches()
    }

    fun setName(name: String) {
        signUpState.value = signUpState.value.copy(name = name)
    }

    fun setEmail(email: String) {
        signUpState.value = signUpState.value.copy(email = email)
    }

    fun setPassword(password: String) {
        signUpState.value = signUpState.value.copy(password = password)
    }

    fun signUp() {
        viewModelScope.launch {
            signUpState.value = signUpState.value.copy(isLoading = true)

            // 2. Генерация "фейкового" токена (для локальной работы)
            val fakeToken = "fake_token_${signUpState.value.email.hashCode()}"

            // 3. Сохраняем данные
            localStorage.setToken(fakeToken) // Токен для "авторизации"
            dataStoreOnBoarding.setOnBoardingCompleted(true) // Помечаем онбординг как пройденный

            // 4. Успешная регистрация
            signUpState.value = signUpState.value.copy(
                isLoading = false,
                isSignUp = true,
                errorMessage = null
            )
        }
    }
}