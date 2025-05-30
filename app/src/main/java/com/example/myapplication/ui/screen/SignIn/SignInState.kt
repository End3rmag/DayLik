package com.example.myapplication.ui.screen.SignIn

data class SignInState (
    var email: String = "",
    var password: String = "",
    var isVisiblePassword: Boolean = false,
    var isLoading: Boolean = false,
    var isSignIn: Boolean = false,
    var errorMessage: String? = null,
    )