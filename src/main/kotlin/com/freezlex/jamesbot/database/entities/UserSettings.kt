package com.freezlex.jamesbot.database.entities

import com.freezlex.jamesbot.database.entities.Guild.uniqueIndex
import org.jetbrains.exposed.sql.Table

object UserSettings: Table("users_settings") {
    val settings_id = integer("id").autoIncrement()
    val settings_lang = varchar("regional_code", 10)
    val settings_user = reference("user_id", User.user_id).uniqueIndex()

    // Never forget to override the Primary key !!
    override val primaryKey = PrimaryKey(this.settings_id, name = "id")
}
