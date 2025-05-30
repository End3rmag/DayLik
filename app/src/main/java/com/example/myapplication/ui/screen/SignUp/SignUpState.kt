package com.example.myapplication.ui.screen.SignUp

data class SignUpState (
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var isVisiblePassword: Boolean = false,
    var isLoading: Boolean = false,
    var isSignUp: Boolean = false,
    var errorMessage: String? = null,
)