package com.qwict.svkandroid.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.qwict.svkandroid.dto.User
import com.qwict.svkandroid.ui.MainViewModel

// TODO: this is deprecated? What should we replace it with...
val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
// TODO: also declare sharedPreference variable here to make code more elegant?

fun getTokenFromSharedPrefs(mainViewModel: MainViewModel, applicationContext: Context) {
    val sharedPreferences = getSharedPreferences(applicationContext)
    val token = sharedPreferences.getString("token", "")

    if (token != null && token != "") {
        if (tokenIsValid(mainViewModel)) {
            Log.i("MainActivity", "onResume: token was found")
            mainViewModel.user = User(
                jwt = token,
            )
            mainViewModel.userIsAuthenticated = true
        } else {
            Log.i("MainActivity", "onResume: token was found but is invalid")
            mainViewModel.logout()
        }
    } else {
        Log.i("MainActivity", "onResume: token is null or empty")
        mainViewModel.logout()
    }
}

fun getPreference(key: String, applicationContext: Context): String {
    val sharedPreferences = getSharedPreferences(applicationContext)
    return sharedPreferences.getString("token", "") ?: ""
}

fun saveEncryptedPreference(key: String, preference: String, applicationContext: Context) {
    val sharedPreferences = getSharedPreferences(applicationContext)
    sharedPreferences.edit().putString(key, preference).apply()

    // TODO remove in production
    Log.i("MainActivity", "onResume: $key was saved to preference file as: '$preference'")
}

fun getSharedPreferences(applicationContext: Context): SharedPreferences {
    return EncryptedSharedPreferences.create(
        "preferences",
        masterKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
}

fun clearEncryptedPreferences(key: String, applicationContext: Context) {
    val sharedPreferences = getSharedPreferences(applicationContext)
    sharedPreferences.edit().clear().apply()
}
