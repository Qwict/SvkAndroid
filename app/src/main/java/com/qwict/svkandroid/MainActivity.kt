package com.qwict.svkandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) {
        }
        launcher.launch( arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.CAMERA
                )
            )
        //launcher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
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
