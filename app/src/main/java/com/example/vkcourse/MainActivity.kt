package com.example.vkcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vkcourse.di.AppContainer
import com.example.vkcourse.presentation.navigation.AppNavHost
import com.example.vkcourse.presentation.theme.VKCourseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKCourseTheme {
                AppNavHost(repository = AppContainer.appRepository)
            }
        }
    }
}
