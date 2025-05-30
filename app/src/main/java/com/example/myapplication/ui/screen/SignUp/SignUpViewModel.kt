package com.example.myapplication.ui.screen.SignUp

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.data.domain.usecase.AuthUseCase
import com.example.myapplication.ui.data.remote.Response.NetworkResponse
import com.example.myapplication.ui.data.remote.User
import kotlinx.coroutines.launch

class SignUpViewModel(val authUseCase: AuthUseCase): ViewModel() {
    var signUpState = mutableStateOf(SignUpState())
        private set

    val emailHasError = derivedStateOf {
        if(signUpState.value.email.isEmpty()) return@derivedStateOf false
        !android.util.Patterns.EMAIL_ADDRESS.matcher(signUpState.value.email).matches()
    }

    fun setName(name: String){
        signUpState.value = signUpState.value.copy(name = name)
    }

    fun setEmail(email: String){
        signUpState.value = signUpState.value.copy(email = email)
    }

    fun setPassword(password: String){
        signUpState.value = signUpState.value.copy(password = password)
    }

    private fun setErrorMessage(message: String) {
        signUpState.value = signUpState.value.copy(errorMessage = message)
    }

    private fun setLoading(isLoading: Boolean) {
        signUpState.value = signUpState.value.copy(isLoading = true)
    }

    fun signUp(){
        viewModelScope.launch {
            val user = User(
                userName = signUpState.value.name,
                email = signUpState.value.email,
                password = signUpState.value.password
            )
           val result = authUseCase.signUp(user)
            result.collect { it ->
                when(it){
                    is NetworkResponse.Error -> {
                        setErrorMessage(it.errorMessage)
                    }
                    is NetworkResponse.Success<*> -> {
                        setLoading(false)
                        signUpState.value  = signUpState.value.copy(isSignUp = true)
                    }
                    is NetworkResponse.Loading -> {
                        setLoading(true)
                    }
                }
            }

        }
    }
}