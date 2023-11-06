package com.qwict.svkandroid.data.local

import android.content.Context
import kotlinx.coroutines.CoroutineScope

interface LocalDataContainer {
//    val localRepo: ALocalRepo
}
class LocalDataContainerImpl(private val context: Context, private val scope: CoroutineScope) :
    LocalDataContainer {
//    override val localRepo: ALocalRepo by lazy {
//        ALocalRepoImpl(Database.getDatabase(context, scope).planetDao())
//    }
}
