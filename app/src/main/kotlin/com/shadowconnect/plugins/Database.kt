package com.shadowconnect.plugins

import com.shadowconnect.db.DatabaseRepositoryImpl
import org.jetbrains.exposed.sql.Database

fun configureDatabase() {
    // Create DB Schema on server
    db.createTables()
}


// This does not "connect" to database but rather provides a connection description for future use.
// Real connections are instantiated inside a 'transaction' block
// Local Database Connection
object DbSettings {
    val db: Database = Database.connect(
        "jdbc:postgresql://localhost:5432/kyleamyx",
        driver = "org.postgresql.Driver",
        user = "kyleamyx"
    )
}

val db = DatabaseRepositoryImpl(DbSettings.db)