package com.freezlex.kohanato.core.cache.extension

import com.freezlex.kohanato.core.database.models.GuildEntity

data class GuildCache(
    override var language: String = "en-EN",
): ICache {
    constructor(guild: GuildEntity): this(guild.language)
}
