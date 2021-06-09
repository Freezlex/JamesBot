package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.sql.Table

/** Guild entity for database transactional */
object Guild: Table("guilds") {
    val guild_id = integer("id").autoIncrement()
    val guild_guildId = long("guild_id").uniqueIndex()
    val guild_name = varchar("name", 100)
    val guild_owner = reference("owner_id", User.user_id)

    // Never forget to override the Primary key !!
    override val primaryKey = PrimaryKey(this.guild_id, name = "id")
}
