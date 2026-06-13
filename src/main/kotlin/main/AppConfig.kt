package main

import java.io.File
import java.io.FileInputStream
import java.util.*

object AppConfig {
    const val APP_PROP_FILE = "app.properties"
    private val keys = listOf("url", "user", "password")
    private val check = mapOf<String, (String) -> Boolean>()

    val properties: Properties = Properties().also { props ->
        if (File(APP_PROP_FILE).exists()) {
            props.load(FileInputStream(File(APP_PROP_FILE)))
        }
    }

    val url: String by lazy { properties.getProperty(keys[0]) }

    val user: String by lazy { properties.getProperty(keys[1]) }

    val password: String by lazy { properties.getProperty(keys[2]) }

    fun isValid() =
        ! properties.isEmpty &&
        keys.all {
            properties.getProperty(it) != null &&
            properties.getProperty(it).isNotEmpty() &&
            (check[it] == null || check[it]?.let { f -> f(properties.getProperty(it)) } == true)
        }
}