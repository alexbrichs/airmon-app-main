package com.airmon.app.helpers

import android.app.Application
import android.database.sqlite.SQLiteDatabase

import io.github.irgaly.kottage.Kottage
import io.github.irgaly.kottage.KottageEnvironment
import io.github.irgaly.kottage.KottageStorage
import io.github.irgaly.kottage.platform.contextOf

import java.io.File

import kotlin.reflect.typeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.serialization.json.Json

suspend inline fun <reified T : Any>retrieveFromDB(application: Application, attribute: String): T? {
    val storage = getStorage(application)
    return storage.getOrNull(attribute, typeOf<T>())
}

suspend inline fun <reified T : Any>storeToDB(
    application: Application,
    attribute: String,
    value: T
) {
    val storage = getStorage(application)
    storage.put(attribute, value, typeOf<T>())
}

suspend fun removeFromDB(application: Application, attribute: String) {
    val storage = getStorage(application)
    storage.remove(attribute)
}

suspend fun getStorage(application: Application): KottageStorage {
    val dbname = "app_configs"
    val file = File(
        application.applicationContext.getDatabasePath(dbname).toString()
    )

    if (!file.exists()) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs()
        }
        SQLiteDatabase.openOrCreateDatabase(file, null)
    }

    val kottageEnvironment = KottageEnvironment(
        context = contextOf(application.applicationContext)
    )
    val kottage = Kottage(
        name = dbname,
        directoryPath = file.parentFile.path,
        environment = kottageEnvironment,
        scope = CoroutineScope(currentCoroutineContext()),
        json = Json
    )
    return kottage.storage(dbname)
}
