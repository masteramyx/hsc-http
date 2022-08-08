package com.shadowconnect.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI
import java.net.URISyntaxException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


/**
 * Database Schema
 */

object Organization : Table() {
    val id = integer("org_id").autoIncrement() // Column<Integer>
    val address = varchar("address", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val phone = varchar("phone", length = 20) // Column<String>
    val website = varchar("website", length = 30) // Column<String>

    override val primaryKey = PrimaryKey(id) // name is optional here
}

object Professional : Table() {
    val id = integer("prof_id").autoIncrement() // Column<Integer>
    val first_name = varchar("f_name", length = 50) // Column<String>
    val last_name = varchar("l_name", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val phone = varchar("phone", length = 20) // Column<String>
    val orgId = (integer("organization_id") references Organization.id).nullable() // Column<Int?>

    override val primaryKey = PrimaryKey(id) // name is optional here
}

object Student : Table() {
    val id = integer("student_id").autoIncrement() // Column<Integer>
    val first_name = varchar("f_name", length = 50) // Column<String>
    val last_name = varchar("l_name", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val phone = varchar("phone", length = 20) // Column<String>
    val orgId = (integer("organization_id") references Organization.id).nullable() // Column<Int?>

    override val primaryKey = PrimaryKey(id) // name is optional here
}

/**
 * Class to handle reading/writing to databsse
 * @see https://github.com/JetBrains/Exposed/wiki/Getting-Started
 */
class DatabaseRepositoryImpl {

    // This does not "connect" to database but rather provides a connection description for future use.
    // Real connections are instantiated inside a 'transaction' block
    var db: Database = Database.connect(
        "jdbc:postgresql://localhost:5432/kyleamyx",
        driver = "org.postgresql.Driver",
        user = "kyleamyx"
    )

    init {
        transaction {
            SchemaUtils.create(Organization, Professional, Student)
        }
    }

    fun testConnection() {
        println("SCHEMA IS  ${db.config.defaultSchema}")
        transaction {
            val q = Organization.selectAll()

            q.forEach {
                println("QUERY IS $it")
            }
        }

    }

    @Throws(URISyntaxException::class, SQLException::class)
    private fun getConnection(): Connection? {
        val dbUri = URI(System.getenv("DATABASE_URL"))
        val username: String = dbUri.getUserInfo().split(":").get(0)
        val password: String = dbUri.getUserInfo().split(":").get(1)
        val dbUrl =
            "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require"
        return DriverManager.getConnection(dbUrl, username, password)
    }
}

