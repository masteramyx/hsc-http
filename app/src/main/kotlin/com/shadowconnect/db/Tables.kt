package com.shadowconnect.db

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Objects representing tables in the application's database
 */

object Organization : IntIdTable() {
    val name = varchar("name", length = 50) // Column<String>
    val address = varchar("address", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val phone = varchar("phone", length = 20) // Column<String>
    val website = varchar("website", length = 30) // Column<String>
}

object Professional : IntIdTable() {
    val first_name = varchar("f_name", length = 50) // Column<String>
    val last_name = varchar("l_name", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val phone = varchar("phone", length = 20) // Column<String>
    val orgId = reference("organization_id", Organization.id).nullable() // Column<Int?>
}

object Student : IntIdTable() {
    val first_name = varchar("f_name", length = 50) // Column<String>
    val last_name = varchar("l_name", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val phone = varchar("phone", length = 20) // Column<String>
    val orgId = reference("organization_id", Organization.id).nullable() // Column<Int?>
}

// Duplicate names for Object and Tables means this or full package name.
typealias Org = com.shadowconnect.model.Organization
typealias OrgList = List<com.shadowconnect.model.Organization>
typealias Stu = com.shadowconnect.model.Student
typealias StudentList = List<com.shadowconnect.model.Student>
typealias ProfList = List<com.shadowconnect.model.Professional>
typealias Prof = com.shadowconnect.model.Professional
