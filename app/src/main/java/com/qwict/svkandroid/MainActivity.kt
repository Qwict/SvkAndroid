package com.qwict.svkandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
//        TODO: This will crash the application, see what goes wrong here
//        val healthService = HealthService()
//        var version = healthService.getHealth()
//        Log.i("MainActivity", "onCreate: $version")
        super.onCreate(savedInstanceState)
        mainViewModel.setContext(this)
        setContent {
            SvkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SvkAndroidApp(mainViewModel)
                }
            }
        }
    }
}
