package com.qwict.svkandroid.common
data class DecodedPayload(
//    val userId: Int,
    val email: String,
//    val role: String,
    val iat: Int,
    val exp: Int,
)
data class DecodedHeader(
    val alg: String,
    val typ: String,
)
