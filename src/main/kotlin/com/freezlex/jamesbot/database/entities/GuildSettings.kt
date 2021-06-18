package com.freezlex.jamesbot.database.entities

import com.freezlex.jamesbot.database.entities.Guild.uniqueIndex
import org.jetbrains.exposed.sql.Table

/** Guild Settings entity for database transactional */
object GuildSettings: Table("guilds_settings") {
    val settings_id = integer("id").autoIncrement()
    val settings_guildId = reference("guild_id", Guild.guild_id).uniqueIndex()
    val settings_prefix = varchar("prefix", 10)
    val settings_lang = varchar("regional_code", 10)


    // Never forget to override the Primary key !!
    override val primaryKey = PrimaryKey(this.settings_id, name = "id")
}
