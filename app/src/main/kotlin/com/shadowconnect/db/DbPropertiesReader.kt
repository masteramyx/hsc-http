package com.shadowconnect.db

import java.io.File
import java.io.FileInputStream
import java.util.*

private const val CONFIG = "db.properties"

object DbPropertiesReader {
    private val properties = Properties()

    init {
        val file = FileInputStream(File(CONFIG).absolutePath)
        properties.load(file)
    }

    fun getProperty(key: String): String = properties.getProperty(key)
}