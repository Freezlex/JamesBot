package com.freezlex.jamesbot.database.entities

import net.dv8tion.jda.api.entities.Guild
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

/** Guild entity for database transactional */
object GuildEntities: IntIdTable("guilds") {
    val guildId = long("guild_id").uniqueIndex()
    val name = varchar("name", 100)
    val owner = reference("owner_id", UserEntities)

    fun findOrCreate(entity: Guild): GuildEntity {
        val result = UserEntities.findOrCreate(entity.owner!!.user)
        return transaction {
            GuildEntity.find { guildId eq entity.idLong }.firstOrNull()?:
                GuildEntity.new {
                guildId = entity.idLong
                name = entity.name
                owner = result
            }
        }
    }
}

class GuildEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<GuildEntity>(GuildEntities)
    var guildId by GuildEntities.guildId
    var name by GuildEntities.name
    var owner by UserEntity referencedOn GuildEntities.owner
}
