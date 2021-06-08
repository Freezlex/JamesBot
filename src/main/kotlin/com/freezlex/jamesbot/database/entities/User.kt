package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.sql.Table

/** Guild entity for database transactional */
object User: Table("users") {
    val id =  integer("id").autoIncrement()
    val usrId = long("user_id").uniqueIndex()
    val username = varchar("username", 40)
    val tag = varchar("tag", 4)

    // Never forget to override the Primary key !!
    override val primaryKey = PrimaryKey(this.id, name = "id")
}
