package com.example.myapplication.ui.screen.RecoverPassword

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RecoverPasswordViewModel: ViewModel() {
    var recoverPasswordState = mutableStateOf(RecoverPasswordState())
        private set

    val emailHasError = derivedStateOf {
        if(recoverPasswordState.value.email.isEmpty()) return@derivedStateOf false
        !android.util.Patterns.EMAIL_ADDRESS.matcher(recoverPasswordState.value.email).matches()
    }

    fun setEmail(email: String){
        recoverPasswordState.value = recoverPasswordState.value.copy(email = email)
    }
    fun showDialog() {
        recoverPasswordState.value = recoverPasswordState.value.copy(showDialog = true)
    }

    fun hideDialog() {
        recoverPasswordState.value = recoverPasswordState.value.copy(showDialog = false)
    }
}