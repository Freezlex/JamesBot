package com.freezlex.kohanato.core.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserEntities: IntIdTable("users") {
    val discordId = long("user_id")
    val permbit = integer("user_perm_bit").default(0)
    val language = varchar("user_lang", 5).default("en-EN")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}

class UserEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<UserEntity>(UserEntities)

    var discordId by UserEntities.discordId
    var perbit by UserEntities.permbit
    var language by UserEntities.language
    val createdAt by UserEntities.createdAt
}