package com.qwict.svkandroid.common.stringRes

import android.content.Context

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(resourceId: Int, args: String?): String {
        return context.getString(resourceId, args)
    }

    override fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }
}
