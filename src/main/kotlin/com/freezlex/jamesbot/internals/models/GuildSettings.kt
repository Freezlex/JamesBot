package com.freezlex.jamesbot.internals.models

import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import com.freezlex.jamesbot.database.repository.GuildRepository
import com.freezlex.jamesbot.database.repository.GuildSettingsRepository
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GuildSettings @Autowired constructor(
    val guildRepository: GuildRepository,
    val guildSettingsRepository: GuildSettingsRepository
){
    /**
     * Get the global bot prefix or the custom guild prefix
     * TODO : Change the GuildMessageReceivedEvent to custom event
     * @param event The message event to get all the properties from the guild
     * @return String The prefix found in the database configuration OR the default bot prefix
     */
    fun getGuildPrefix(event: GuildMessageReceivedEvent):String{
        var setting: GuildSettingsEntity? = settings.find{ settings -> settings.guild.guildId == event.guild.idLong}
        if(setting == null){ // First to check to fetch database if we don't have any result
            setting = guildSettingsRepository.findByGuild_GuildId(event.guild.idLong)
            if(setting != null)settings.add(setting)
        }
        if(setting?.prefix != null)return setting.prefix
        return System.getenv("PREFIX")
    }

    companion object {
        private val settings = mutableListOf<GuildSettingsEntity>();
    }
}
