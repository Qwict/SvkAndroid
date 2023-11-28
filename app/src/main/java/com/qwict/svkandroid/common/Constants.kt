package com.qwict.svkandroid.common

import com.qwict.svkandroid.domain.model.User

object Constants {
    const val BASE_URL = "https://svk.qwict.com/api/"
//        "http://10.0.2.2:9010/api/"

    const val BLOB_URL = "https://svk.mstuyven.workers.dev/"

    val EMPTY_USER = User(
        email = "",
        role = "No Role",
    )
}
