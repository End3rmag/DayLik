package com.example.myapplication.ui.data.remote.dto.response

import com.example.myapplication.ui.data.remote.Request.LoginRequest
import com.example.myapplication.ui.data.remote.User
import retrofit2.http.Body
import retrofit2.http.POST

interface Auth {
    @POST("/registration")
    suspend fun registration(@Body user: User): TokenResponse

    @POST("/authorization")
    suspend fun authorization(@Body loginRequest: LoginRequest): TokenResponse
}