package com.freezlex.jamesbot.internals.models

import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import com.freezlex.jamesbot.database.repository.GuildRepository
import com.freezlex.jamesbot.database.repository.GuildSettingsRepository
import com.freezlex.jamesbot.internals.utils.Utility
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GuildCache @Autowired constructor(
    val guildRepository: GuildRepository,
    val guildSettingsRepository: GuildSettingsRepository
){

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Get the global bot prefix or the custom guild prefix
     * TODO : Change the GuildMessageReceivedEvent to custom event
     * @param event The message event to get all the properties from the guild
     * @return String The prefix found in the database configuration OR the default bot prefix
     */
    fun getCachedSettings(event: GuildMessageReceivedEvent):GuildSettings {
        logger.info("Trying to find guild settings into the cache")
        var guildSettings: GuildSettings? = settings.find{ settings -> settings.guildId == event.guild.idLong}
        if(guildSettings == null){
            logger.info("Trying to find guild settings into the database and adding it to the cache if not null")
            val dbFoundSettings = guildSettingsRepository.findByGuild_GuildId(event.guild.idLong)
            if(dbFoundSettings == null){
                logger.info("Building new GuildSettings and adding it to the cache")
                guildSettings = GuildSettings(event.guild.idLong, System.getenv("PREFIX"), getCommandPattern(System.getenv("PREFIX"), event))
                settings.add(guildSettings)
            }else{
                guildSettings = GuildSettings(dbFoundSettings.guild.guildId, dbFoundSettings.prefix, getCommandPattern(dbFoundSettings.prefix, event))
                settings.add(guildSettings)
            }
        }
        return guildSettings;
    }

    fun getCommandPattern(prefix: String, event: GuildMessageReceivedEvent): Regex{
        logger.info("Building command pattern for prefix : $prefix")
        val escapedPrefix: String = Utility.escapeRegex(prefix);
        return Regex("^(<@!?${event.jda.selfUser.id}>\\s+(?:${escapedPrefix}\\s*)?|${escapedPrefix})([^\\s]+)", RegexOption.IGNORE_CASE)
    }

    companion object {
        private val settings = mutableListOf<GuildSettings>();
    }
}
