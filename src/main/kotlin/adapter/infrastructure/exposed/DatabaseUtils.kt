package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun connectToDatabase(url: String, user: String, password: String) =
    try {
        Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        ).also {
            transaction(it) { exec("SELECT 1") }
        }
        true
    } catch (e: Exception) {
        false
    }