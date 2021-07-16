package com.freezlex.jamesbot.database.entities

import com.freezlex.jamesbot.internals.i18n.LanguageList
import com.freezlex.jamesbot.internals.client.ClientSettings
import net.dv8tion.jda.api.entities.Guild
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

/** Guild Settings entity for database transactional */
object GuildsSettings: IntIdTable("guilds_settings") {
    val guild = reference("guild", GuildEntities).uniqueIndex()
    val prefix = varchar("prefix", 10)
    val regCde = varchar("regional_code", 10)

    fun findOrCreate(entity: Guild): GuildSettings{
        val result = GuildEntities.findOrCreate(entity)
        return transaction {
            GuildSettings.find { guild eq result.id }.firstOrNull()?:
            GuildSettings.new {
                guild = result
                prefix = ClientSettings.prefix
                regCde = ClientSettings.language
            }
        }
    }
}

class GuildSettings(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<GuildSettings>(GuildsSettings)
    var guild by GuildEntity referencedOn GuildsSettings.guild
    var prefix by GuildsSettings.prefix
    var regCde by GuildsSettings.regCde
}
