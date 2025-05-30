package com.example.myapplication.ui.data.remote.Response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val first: String,
    val second: String
)

