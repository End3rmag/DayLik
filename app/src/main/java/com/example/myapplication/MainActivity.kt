package com.example.myapplication

import CalendarScrn
import HomeScrn
import SplashScreen
import TasksViewModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.ui.data.Worker.DailyNotificationsWorker
import com.example.myapplication.ui.theme.MatuleTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            val workRequest = OneTimeWorkRequestBuilder<DailyNotificationsWorker>()
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)

// Для отслеживания статуса Worker’а
            WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequest.id)
                .observe(this) { workInfo ->
                    Log.d("WorkerTest", "Work state: ${workInfo.state}")
                }
        }
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