package com.example.myapplication.di

//import com.example.myapplication.ui.data.local.repository.AuthLocalRepository
//import com.example.myapplication.ui.data.local.repository.TaskLocalRepository
//import com.example.myapplication.ui.screen.SignIn.SignInViewModel
//import com.example.myapplication.ui.screen.SignUp.SignUpViewModel
//import com.example.myapplication.ui.screen.RecoverPassword.RecoverPasswordViewModel
//import com.example.myapplication.ui.screen.tasks.TasksViewModel
import TasksViewModel
import com.example.myapplication.ui.data.Worker.NotificationsManagement
import com.example.myapplication.ui.data.local.AppDatabase
import com.example.myapplication.ui.data.local.repository.TaskRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().taskDao() }
    single { TaskRepository(get()) }
    single { NotificationsManagement (androidContext()) }
    viewModel { TasksViewModel(get()) }
}