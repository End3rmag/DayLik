package com.example.myapplication.ui.data.remote.dto.response

import android.provider.ContactsContract.CommonDataKinds.Website.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofit by lazy {
        retrofitBuilder.create(Auth::class.java)
    }
}