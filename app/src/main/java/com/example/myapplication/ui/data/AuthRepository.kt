package com.example.myapplication.ui.data

import com.example.myapplication.ui.data.remote.dto.response.Auth
import com.example.myapplication.ui.data.remote.User
import com.example.myapplication.ui.data.remote.Request.LoginRequest
import com.example.myapplication.ui.data.remote.dto.response.TokenResponse
import kotlinx.coroutines.delay

class AuthRepository(private val api: Auth) {
    suspend fun signUp(user: User): TokenResponse {
        delay(3000)
        return api.registration(user)
    }
    suspend fun signIn(loginRequest: LoginRequest): TokenResponse {
        delay(3000)
        return api.authorization(loginRequest)
    }

}