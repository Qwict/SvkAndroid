package com.qwict.svkandroid.data.local

import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.qwict.svkandroid.SvkAndroidApplication

// TODO: this is deprecated? What should we replace it with...
val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
// TODO: also declare sharedPreference variable here to make code more elegant?

fun getEncryptedPreference(key: String): String {
    return getSharedPreferences().getString(key, "") ?: ""
}

fun saveEncryptedPreference(key: String, preference: String) {
    getSharedPreferences().edit().putString(key, preference).apply()
    // TODO remove in production
    Log.d("EncryptedPreference", "EncryptedPreference saved: [ $key : $preference ]")
}

fun removeEncryptedPreference(key: String) {
    getSharedPreferences().edit().remove(key).apply()
    Log.d("EncryptedPreference", "EncryptedPreference removed: [ $key ]")
}

private fun getSharedPreferences(): SharedPreferences {
    return EncryptedSharedPreferences.create(
        "preferences",
        masterKeyAlias,
        SvkAndroidApplication.appContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
}
