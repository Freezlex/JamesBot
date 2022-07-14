package com.freezlex.kohanato.core.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object GuildEntities : IntIdTable("guilds"){
    val discordId = integer("discord_id")
    val owner = reference("owner", UserEntities)
    val language = varchar("language", 5).default("en-EN")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}

class GuildEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<GuildEntity>(GuildEntities)

    var discordId by GuildEntities.discordId
    var owner by GuildEntities.owner
    var language by GuildEntities.language
    var createdAt by GuildEntities.createdAt
}