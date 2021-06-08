package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.sql.Table

/** Guild entity for database transactional */
object Guild: Table("guilds") {
    val id = integer("id").autoIncrement()
    val guildId = long("guild_id")
    val name = varchar("name", 100)
    val owner = reference("owner_id", User.id).uniqueIndex()

    // Never forget to override the Primary key !!
    override val primaryKey = PrimaryKey(this.id, name = "id")
}
