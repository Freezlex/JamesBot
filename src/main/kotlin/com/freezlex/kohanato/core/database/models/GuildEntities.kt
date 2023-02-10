package com.freezlex.kohanato.core.database.models

import com.freezlex.kohanato.core.database.models.UserEntities.default
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object GuildEntities : IntIdTable("guilds"){
    val DiscordId = integer("gds_discord_id")
    val Owner = reference("owner", UserEntities)
    val Language = varchar("language", 5).default("en-EN")
    val CreatedAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val CreatedBy = GuildEntities.long("usr_created_by").default(ProcessHandle.current().pid())
}

class GuildEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<GuildEntity>(GuildEntities)

    var DiscordId by GuildEntities.DiscordId
    var Owner by UserEntity referencedOn GuildEntities.Owner
    var Language by GuildEntities.Language
    var CreatedAt by GuildEntities.CreatedAt
    var CreatedBy by GuildEntities.CreatedBy
}
