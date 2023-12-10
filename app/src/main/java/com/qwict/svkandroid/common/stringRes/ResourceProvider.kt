package com.qwict.svkandroid.common.stringRes

interface ResourceProvider {
    fun getString(resourceId: Int, args: String?): String
    fun getString(resourceId: Int): String
}
