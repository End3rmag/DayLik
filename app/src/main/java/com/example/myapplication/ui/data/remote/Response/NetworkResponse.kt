package com.example.myapplication.ui.data.remote.Response

sealed class NetworkResponse {
    data class Success<T>(val data: T): NetworkResponse()
    data object Loading: NetworkResponse()
    data class Error(val errorMessage: String): NetworkResponse()
}