package com.shadowconnect.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Class to handle reading/writing to database
 * @see https://github.com/JetBrains/Exposed/wiki/Getting-Started
 *
 */
class DatabaseRepositoryImpl(val database: Database) {

    fun createTables() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Organization, Professional, Student)
        }
    }

    // region organization
    fun getOrganizations(): OrgList {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Organization.selectAll()
            return@transaction mutableListOf<Org>().apply {
                queryResult.map { row -> add(row.toOrganization()) }
            }
        }
    }

    fun getOrganizationById(id: Int): Org? {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Organization.select {
                Organization.id eq id
            }

            if (queryResult.empty()) {
                return@transaction null
            } else {
                return@transaction queryResult.map { row -> row.toOrganization() }.firstOrNull()
            }
        }
    }

    fun addOrganization(org: Org) {
        transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            val id = Organization.insertAndGetId {
                it[name] = org.name
                it[email] = org.email
                it[address] = org.address
                it[phone] = org.phone
                it[website] = org.website
            }
            println("NEW ID: $id")
        }
    }

    fun removeOrganization(id: Int): Boolean {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val numberOfDeletedRows = Organization.deleteWhere {
                Organization.id eq id
            }
            return@transaction numberOfDeletedRows > 0
        }
    }

    //todo get this func working and done clean
    fun updateOrganization(organization: Org): Boolean {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val organizationUpdated = Organization.update(
                where = { Organization.id eq organization.id }
            ) {
                it[name] = organization.name
                it[address] = organization.address
                it[email] = organization.email
                it[phone] = organization.phone
                it[website] = organization.website
            }
            return@transaction organizationUpdated > 0
        }
    }

    // endregion

    // region professional
    fun getProfessionals(): ProfList {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Professional.selectAll()
            return@transaction mutableListOf<Prof>().apply {
                queryResult.map { row -> add(row.toProfessional(null)) }
            }
        }
    }

    fun getProfessionalById(id: Int): Prof? {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Professional.select {
                Professional.id eq id
            }
            return@transaction queryResult.map { row -> row.toProfessional(id) }.firstOrNull()
        }
    }

    fun addProfessional(professional: Prof) {
        transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            val id = Professional.insertAndGetId {
                it[first_name] = professional.firstName
                it[last_name] = professional.lastName
                it[email] = professional.email
                it[phone] = professional.phone
                it[orgId] = professional.orgId
            }
            println("NEW ID: $id")
        }
    }

    fun removeProfessional(id: Int): Boolean {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val numberOfDeletedRows = Professional.deleteWhere {
                Professional.id eq id
            }
            return@transaction numberOfDeletedRows > 0
        }
    }

    //todo get this func working and done clean
    fun updateProfessional(professional: Prof): Boolean {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            val professionalUpdated = Professional.update(
                where = { Professional.id eq professional.id }
            ) {
                it[first_name] = professional.firstName
                it[last_name] = professional.lastName
                it[email] = professional.email
                it[phone] = professional.phone
                it[orgId] = professional.orgId
            }
            return@transaction professionalUpdated > 0
        }
    }

    // endregion

    // region students
    fun getStudents(): StudentList {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Student.selectAll()
            return@transaction mutableListOf<Stu>().apply {
                queryResult.map { row -> add(row.toStudent()) }
            }
        }
    }

    fun getStudentById(id: Int): Stu? {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Student.select {
                Student.id eq id
            }
            return@transaction queryResult.map { row -> row.toStudent() }.firstOrNull()
        }
    }


    fun addStudent(student: Stu) {
        transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            val id = Student.insertAndGetId {
                it[first_name] = student.firstName
                it[last_name] = student.lastName
                it[email] = student.email
                it[phone] = student.phone
                it[orgId] = student.orgId
            }
            println("NEW ID: $id")
        }
    }

    fun removeStudent(id: Int): Boolean {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val numberOfDeletedRows = Student.deleteWhere {
                Student.id eq id
            }
            return@transaction numberOfDeletedRows > 0
        }
    }

    //todo get this func working and done clean
    fun updateStudent(student: Stu): Boolean {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            val studentUpdated = Student.update(
                where = { Student.id eq student.id }
            ) {
                it[first_name] = student.firstName
                it[last_name] = student.lastName
                it[email] = student.email
                it[phone] = student.phone
                it[orgId] = student.orgId
            }
            return@transaction studentUpdated > 0
        }
    }


    // endregion
}


private fun ResultRow.toOrganization() =
    Org(
        name = this[Organization.name],
        address = this[Organization.address],
        email = this[Organization.email],
        phone = this[Organization.phone],
        website = this[Organization.website]
    )

private fun ResultRow.toProfessional(id: Int?) =
    Prof(
        id = id,
        firstName = this[Professional.first_name],
        lastName = this[Professional.last_name],
        email = this[Professional.email],
        phone = this[Professional.phone],
        orgId = this[Professional.orgId]?.value
    )

private fun ResultRow.toStudent() =
    Stu(
        firstName = this[Student.first_name],
        lastName = this[Student.last_name],
        phone = this[Student.phone],
        orgId = this[Student.orgId]?.value,
        email = this[Student.email]
    )