package com.shadowconnect.plugins

import com.shadowconnect.db.DatabaseRepositoryImpl
import com.shadowconnect.db.Stu
import org.jetbrains.exposed.sql.Database

fun configureDatabase() {
    // TESTING HEERE REMOVE!!!
    db.apply {
        addStudent(
            Stu(
                firstName = "Kyle",
                lastName = "Amyx",
                email = "masteramyx@gmail.com",
                phone = "4045397567",
                orgId = null
            )
        )
    }
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