package com.qwict.svkandroid.common

import com.qwict.svkandroid.domain.model.User

/**
 * Object containing constant values used in the application.
 */
object Constants {
    /**
     * Base URL for the SVK API.
     */
    const val SVK_URL = "https://svk.qwict.com/api/"
//        "http://10.0.2.2:9010/api/" // For local development with emulator

    /**
     * Base URL for the Blob API.
     */
    const val BLOB_URL = "https://svk.mstuyven.workers.dev/"

    val EMPTY_USER = User(
        email = "",
        role = "No Role",
    )

    const val ROLE_LOADER = "Loader"
}
