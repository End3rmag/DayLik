package com.example.myapplication.di

import com.example.myapplication.ui.data.AuthRepository
import com.example.myapplication.ui.data.domain.usecase.AuthUseCase
import com.example.myapplication.ui.data.local.LocalStorage
import com.example.myapplication.ui.data.remote.Auth
import com.example.myapplication.ui.data.remote.RetrofitClient
import com.example.myapplication.ui.screen.RecoverPassword.RecoverPasswordViewModel
import com.example.myapplication.ui.screen.SignIn.SignInViewModel
import com.example.myapplication.ui.screen.SignUp.SignUpViewModel
import com.squareup.wire.get
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val appModules = module {
    single <LocalStorage> { LocalStorage(get()) }
    single<Auth> { RetrofitClient.retrofit }
    single<AuthRepository> { AuthRepository(get()) }
    single<AuthUseCase> { AuthUseCase(get(), get()) }
    viewModel<SignUpViewModel> { SignUpViewModel (get()) }
    viewModel<SignInViewModel> { SignInViewModel () }
    viewModel<RecoverPasswordViewModel> { RecoverPasswordViewModel () }




}