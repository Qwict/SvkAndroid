package com.qwict.svkandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.qwict.svkandroid.helper.getTokenFromSharedPrefs
import com.qwict.svkandroid.helper.saveEncryptedPreference
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.setContext(this)
        getTokenFromSharedPrefs(mainViewModel)
        setContent {
            // A surface container using the 'background' color from the theme
            SvkAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SvkAndroidApp(mainViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getTokenFromSharedPrefs(mainViewModel)
    }

    override fun onPause() {
        super.onPause()
        saveEncryptedPreference("token", mainViewModel.user.token)
    }
}
