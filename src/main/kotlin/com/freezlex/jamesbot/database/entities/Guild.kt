package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.sql.Table

/** Guild entity for database transactional */
object Guild: Table("guilds") {
    val id = integer("id").autoIncrement()
    val guildId = long("guild_id").uniqueIndex()
    val name = varchar("name", 100)
    val owner = integer("owner_id").references(User.id)
}
