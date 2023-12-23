package com.qwict.svkandroid

import com.qwict.svkandroid.common.stringRes.ResourceProvider

class FakeResourceProvider : ResourceProvider {
    override fun getString(resourceId: Int, args: String?): String {
        return "Fake string with args: $args"
    }

    override fun getString(resourceId: Int): String {
        return "Fake string"
    }
}
