package com.shadowconnect.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Class to handle reading/writing to database
 * @see https://github.com/JetBrains/Exposed/wiki/Getting-Started
 *
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
                queryResult.map { row ->
                    add(
                        // todo extenstion functions for all
                        Org(
                            name = row[Organization.name],
                            address = row[Organization.address],
                            email = row[Organization.email],
                            phone = row[Organization.phone],
                            website = row[Organization.website]
                        )
                    )
                }
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
                return@transaction queryResult.map { row ->
                    Org(
                        name = row[Organization.name],
                        address = row[Organization.address],
                        email = row[Organization.email],
                        phone = row[Organization.phone],
                        website = row[Organization.website]
                    )
                }.firstOrNull()
            }
        }
    }

    fun addOrganization(org: Org) {
        transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            Organization.insert {
                it[name] = org.name
                it[email] = org.email
                it[address] = org.address
                it[phone] = org.phone
                it[website] = org.website
            } get Organization.id
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

    // endregion

    // region professional
    fun getProfessionals(): ProfList {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Professional.selectAll()
            return@transaction mutableListOf<Prof>().apply {
                queryResult.map { row ->
                    add(
                        Prof(
                            first_name = row[Professional.first_name],
                            last_name = row[Professional.last_name],
                            email = row[Professional.email],
                            phone = row[Professional.phone],
                            org_id = row[Professional.orgId]?.value
                        )
                    )
                }
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
            return@transaction queryResult.map { row ->
                Prof(
                    first_name = row[Professional.first_name],
                    last_name = row[Professional.last_name],
                    email = row[Professional.email],
                    phone = row[Professional.phone],
                    org_id = row[Professional.orgId]?.value
                )
            }.firstOrNull()
        }
    }

    fun addProfessional(professional: Prof) {
        transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)

            Professional.insert {
                it[first_name] = professional.first_name
                it[last_name] = professional.last_name
                it[email] = professional.email
                it[phone] = professional.phone
                it[orgId] = professional.org_id
            } get Professional.id
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

    // endregion

    // region students
    fun getStudents(): StudentList {
        return transaction {
            // Log our SQL Queries
            addLogger(StdOutSqlLogger)
            val queryResult = Student.selectAll()
            return@transaction mutableListOf<Stu>().apply {
                queryResult.map { row ->
                    add(
                        Stu(
                            firstName = row[Student.first_name],
                            lastName = row[Student.last_name],
                            phone = row[Student.phone],
                            orgId = row[Student.orgId]?.value,
                            email = row[Student.email]
                        )
                    )
                }
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
            return@transaction queryResult.map { row ->
                Stu(
                    firstName = row[Student.first_name],
                    lastName = row[Student.last_name],
                    phone = row[Student.phone],
                    orgId = row[Student.orgId]?.value,
                    email = row[Student.email]
                )
            }.firstOrNull()
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


    // endregion
}