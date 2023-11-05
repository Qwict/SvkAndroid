package com.qwict.svkandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Will check local token and set isUserAuthenticated to true if valid, otherwise will logout()
//        AuthenticationSingleton.validateUser()
        setContent {
            // A surface container using the 'background' color from the theme
            SvkAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SvkAndroidApp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AuthenticationSingleton.validateUser()
    }

    override fun onPause() {
        super.onPause()
//        Used to save token here as well, don't think this is needed anymore
    }
}
