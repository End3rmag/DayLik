package com.example.myapplication.di

import TasksViewModel
import android.content.Context
import androidx.room.Room
import com.example.myapplication.ui.data.local.AppDatabase
import com.example.myapplication.ui.data.local.LocalStorage
//import com.example.myapplication.ui.data.local.repository.AuthLocalRepository
//import com.example.myapplication.ui.data.local.repository.TaskLocalRepository
import com.example.myapplication.ui.data.local.repository.TaskRepository
//import com.example.myapplication.ui.screen.SignIn.SignInViewModel
//import com.example.myapplication.ui.screen.SignUp.SignUpViewModel
//import com.example.myapplication.ui.screen.RecoverPassword.RecoverPasswordViewModel
//import com.example.myapplication.ui.screen.tasks.TasksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().taskDao() }
    single { TaskRepository(get()) }
    viewModel { TasksViewModel(get()) }
}