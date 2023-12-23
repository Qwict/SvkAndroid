package com.qwict.svkandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point activity for the application.
 *
 * This activity is responsible for initializing the application, requesting necessary permissions,
 * and managing the lifecycle of the application.
 *
 * @constructor Creates an instance of [MainActivity].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     *
     * Responsible for setting up the activity, requesting necessary permissions, and
     * initializing the application's UI.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) {
        }
        launcher.launch(
            arrayOf(
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.CAMERA,
            ),
        )
        // launcher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
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

    /**
     * Called when the activity is resumed.
     *
     * Performs user authentication validation when the activity is resumed.
     */
    override fun onResume() {
        super.onResume()
        AuthenticationSingleton.validateUser()
    }

    /**
     * Called when the activity is paused.
     *
     * Used to perform cleanup or save state if necessary.
     */
    override fun onPause() {
        super.onPause()
//        Used to save token here as well, don't think this is needed anymore
    }
}
