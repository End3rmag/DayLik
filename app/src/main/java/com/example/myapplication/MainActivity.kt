package com.example.myapplication

import CalendarScrn
import HomeScrn
import SplashScreen
import TasksViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.data.domain.usecase.AuthUseCase
import com.example.myapplication.ui.local.DataStoreOnBoarding
import com.example.myapplication.ui.screen.Otp.OptScrn
import com.example.myapplication.ui.screen.RecoverPassword.RecoverPasswordScrn
import com.example.myapplication.ui.screen.SignIn.SignInScrn
import com.example.myapplication.ui.screen.SignUp.SignUpScrn
import com.example.myapplication.ui.theme.MatuleTheme
import kotlinx.serialization.Serializable
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.myapplication.ui.data.remote.Tasks.TaskRepository
import com.example.myapplication.ui.data.remote.Tasks.TasksViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val dataStore = DataStoreOnBoarding(LocalContext.current)
            val authUseCase: AuthUseCase = get()

            // Получаем ViewModel через Koin
            val tasksViewModel: TasksViewModel by viewModel()

            MatuleTheme {
                NavHost(navController, startDestination = SplashScreen) {

                    composable<SplashScreen> {
                        SplashScreen(
                            authUseCase = authUseCase,
                            dataStore = dataStore,
                            onNavigateToSignIn = {
                                navController.navigate(route = SignIn) {
                                    popUpTo(SplashScreen) { inclusive = true }
                                }
                            },
                            onNavigateToHome = {
                                navController.navigate(route = Home) {
                                    popUpTo(SplashScreen) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<SignIn> {
                        SignInScrn(
                            navController = navController,
                            onSignInSuccess = {
                                navController.navigate(route = Otp)
                            }
                        )
                    }

                    composable<RecoverPassword> {
                        RecoverPasswordScrn(navController)
                    }

                    composable<Registration> {
                        SignUpScrn(
                            onNavigationToHome = {
                                navController.navigate(route = Home)
                            },
                            navController = navController
                        )
                    }

                    composable<Otp> {
                        OptScrn {
                            navController.navigate(route = Home)
                        }
                    }

                    composable<Home> {
                        val tasksViewModel: TasksViewModel by viewModel()
                        HomeScrn(
                            onNavigateToCalendar = {
                                navController.navigate(route = Calendar)
                            },
                            tasksViewModel = tasksViewModel
                        )
                    }

                    composable<Calendar> {
                        CalendarScrn(
                            onBack = { navController.popBackStack() },
                            tasksViewModel = tasksViewModel
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object SplashScreen
@Serializable
object Registration
@Serializable
object Profile
@Serializable
object RecoverPassword
@Serializable
object SignIn
@Serializable
object Otp
@Serializable
object Slides
@Serializable
object Home
@Serializable
object Calendar