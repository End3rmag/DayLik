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
import com.example.myapplication.ui.local.DataStoreOnBoarding
import com.example.myapplication.ui.screen.Otp.OptScrn
import com.example.myapplication.ui.screen.RecoverPassword.RecoverPasswordScrn
import com.example.myapplication.ui.screen.SignIn.SignInScrn
import com.example.myapplication.ui.screen.SignUp.SignUpScrn
import com.example.myapplication.ui.theme.MatuleTheme
import kotlinx.serialization.Serializable
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val tasksViewModel: TasksViewModel by viewModel()

            MatuleTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onNavigateToHome = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Home.route) {
                        HomeScrn(
                            onNavigateToCalendar = {
                                navController.navigate(Screen.Calendar.route)
                            },
                            tasksViewModel = tasksViewModel
                        )
                    }

                    composable(Screen.Calendar.route) {
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

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object SignIn : Screen("signin")
    object RecoverPassword : Screen("recoverpassword")
    object Otp : Screen("otp")
    object SignUp : Screen("signup")
    object Profile : Screen("profile")
    object Registration : Screen("registration")
    object Slides : Screen("slides")
}