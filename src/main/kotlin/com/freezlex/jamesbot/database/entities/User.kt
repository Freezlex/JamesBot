package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.sql.Table

/** Guild entity for database transactional */
object User: Table("users") {
    val user_id =  integer("id").autoIncrement()
    val user_userId = long("user_id").uniqueIndex()
    val user_username = varchar("username", 40)
    val user_tag = varchar("tag", 4)

    // Never forget to override the Primary key !!
    override val primaryKey = PrimaryKey(this.user_id, name = "id")
}
