package com.qwict.svkandroid.common

import com.qwict.svkandroid.domain.model.User
import java.util.UUID

object Constants {
    const val BASE_URL = "https://svk.qwict.com/api/"
//        "http://10.0.2.2:9010/api/"

    const val BLOB_URL = "https://svk.mstuyven.workers.dev/"

    val DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    const val DEFAULT_ID = 0

    val EMPTY_USER = User(
        email = "",
        firstName = "",
        lastName = "",
        role = "forklift_driver",
    )
}
