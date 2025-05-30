package com.example.myapplication.ui.data.remote

import com.example.myapplication.ui.data.remote.dto.LoginRequest
import com.example.myapplication.ui.data.remote.dto.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Auth {
    @POST("/registration")
    suspend fun registration(@Body user: User): TokenResponse

    @POST("/authorization")
    suspend fun authorization(@Body loginRequest: LoginRequest): TokenResponse
}