package com.freezlex.jamesbot.internals.models

class GuildSettings (
    val guildId: Long,
    var prefix: String,
    val pattern: Regex
        )
