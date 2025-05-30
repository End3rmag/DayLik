package com.example.myapplication.ui.screen.RecoverPassword

data class RecoverPasswordState (
    var email: String = "",
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var showDialog: Boolean = false

)