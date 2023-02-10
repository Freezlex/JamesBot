package com.freezlex.kohanato.core.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserEntities: IntIdTable("users") {
    val DiscordId = long("usr_user_id_val")
    val Flag = integer("usr_flag_val").default(0)
    val Language = varchar("usr_lang_val", 5).default("en-EN")
    val CreatedAt = datetime("usr_created_at").defaultExpression(CurrentDateTime)
    val CreatedBy = long("usr_created_by").default(ProcessHandle.current().pid())
}

class UserEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<UserEntity>(UserEntities)

    var DiscordId by UserEntities.DiscordId
    var Flag by UserEntities.Flag
    var Language by UserEntities.Language
    val CreatedAt by UserEntities.CreatedAt
    val CreatedBy by UserEntities.CreatedBy
}
