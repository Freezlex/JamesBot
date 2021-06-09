package com.freezlex.jamesbot.internals.client

import com.freezlex.jamesbot.internals.api.Utility
import com.freezlex.jamesbot.internals.entities.UserSettings
import com.freezlex.jamesbot.internals.entities.GuildSettings
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.lang.RuntimeException

class ClientCache {
    private val guildCache: MutableList<GuildSettings> = mutableListOf()
    private val userCache: MutableList<UserSettings> = mutableListOf()

    fun setPrefix() : ClientCache{
        return this;
    }

    fun getPrefix(message: Message) : String = getCachedGuildSettings(message).prefix

    fun getPrefixPattern(message: Message): Regex = getCachedGuildSettings(message).pattern

    /**
     * Find the settings of a guild
     * @param guildId The id of the settings guild to find
     */
    fun getCachedGuildSettings(message: Message) : GuildSettings{
        var settings = guildCache.firstOrNull { it.guildId == message.guild.idLong }
        if(settings == null){
            guildCache.add(GuildSettings(message))
            settings = guildCache.first { it.guildId == message.guild.idLong }
        }
        return settings;
    }
}
