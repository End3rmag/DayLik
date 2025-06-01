package com.example.myapplication.di

import AppDatabase
import TasksViewModel
import com.example.myapplication.ui.data.AuthRepository
import com.example.myapplication.ui.data.domain.usecase.AuthUseCase
import com.example.myapplication.ui.data.local.LocalStorage
import com.example.myapplication.ui.data.remote.Tasks.TaskRepository
import com.example.myapplication.ui.data.remote.dto.response.Auth
import com.example.myapplication.ui.data.remote.dto.response.RetrofitClient
import com.example.myapplication.ui.screen.RecoverPassword.RecoverPasswordViewModel
import com.example.myapplication.ui.screen.SignIn.SignInViewModel
import com.example.myapplication.ui.screen.SignUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// di.kt
val appModules = module {
    // Auth и Local Storage зависимости
    single<LocalStorage> { LocalStorage(get()) }
    single<Auth> { RetrofitClient.retrofit }
    single<AuthRepository> { AuthRepository(get()) }
    single<AuthUseCase> { AuthUseCase(get(), get()) }

    // ViewModels для аутентификации
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel() }
    viewModel { RecoverPasswordViewModel() }

    // Room Database
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().taskDao() }
    single { TaskRepository(get()) }

    // Tasks ViewModel
    viewModel { TasksViewModel(get()) }
}
