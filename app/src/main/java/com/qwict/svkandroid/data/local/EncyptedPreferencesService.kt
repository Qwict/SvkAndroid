package com.qwict.svkandroid.data.local

import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.qwict.svkandroid.SvkAndroidApplication

// TODO: this is deprecated? What should we replace it with...
val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
// TODO: also declare sharedPreference variable here to make code more elegant?

/**
 * Retrieves an encrypted preference using the specified [key].
 *
 * @param key The key used to retrieve the encrypted preference.
 * @return The encrypted preference value associated with the given key. Returns an empty string if the key is not found.
 */
fun getEncryptedPreference(key: String): String {
    return getSharedPreferences().getString(key, "") ?: ""
}

/**
 * Saves an encrypted preference using the specified [key] and [preference].
 *
 * @param key The key used to save the encrypted preference.
 * @param preference The value of the encrypted preference to be saved.
 */
fun saveEncryptedPreference(key: String, preference: String) {
    getSharedPreferences().edit().putString(key, preference).apply()
    // TODO remove in production
    Log.d("EncryptedPreference", "EncryptedPreference saved: [ $key : $preference ]")
}

/**
 * Removes an encrypted preference associated with the specified [key].
 *
 * @param key The key used to remove the encrypted preference.
 */
fun removeEncryptedPreference(key: String) {
    getSharedPreferences().edit().remove(key).apply()
    Log.d("EncryptedPreference", "EncryptedPreference removed: [ $key ]")
}

/**
 * Retrieves the [SharedPreferences] instance for handling encrypted preferences.
 *
 * @return The [SharedPreferences] instance for handling encrypted preferences.
 */
private fun getSharedPreferences(): SharedPreferences {
    return EncryptedSharedPreferences.create(
        "preferences",
        masterKeyAlias,
        SvkAndroidApplication.appContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
}
