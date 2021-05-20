package com.freezlex.jamesbot.internals.models

import com.freezlex.jamesbot.database.entity.GuildPermissionEntity

class GuildSettings (
    val guildId: Long,
    var prefix: String,
    val pattern: Regex,
    val permission: GuildPermissionEntity)
