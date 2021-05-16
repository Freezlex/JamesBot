package com.freezlex.jamesbot.internals.models

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import com.freezlex.jamesbot.database.entity.UserEntity
import com.freezlex.jamesbot.database.repository.GuildRepository
import com.freezlex.jamesbot.database.repository.GuildSettingsRepository
import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.database.repository.UserRepository
import com.freezlex.jamesbot.internals.utils.Utility
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GuildCache @Autowired constructor(
    val userRepository: UserRepository,
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
        logger.info("Trying to find settings into the cache for guild ${event.guild.id} ...")
        var guildSettings: GuildSettings? = settings.find{ settings -> settings.guildId == event.guild.idLong}
        if(guildSettings == null){
            logger.info(" No cache found, now trying to find guild settings into the database ...")
            val dbFoundSettings = guildSettingsRepository.findByGuild_GuildId(event.guild.idLong).orElse(null)
            if(dbFoundSettings == null){
                logger.info("No database guild settings fount, creating a brand new one ...")
                guildSettings = GuildSettings(event.guild.idLong, System.getenv("PREFIX"), getCommandPattern(System.getenv("PREFIX"), event))
                settings.add(guildSettings)
            }else{
                logger.info("Settings for the guild found in the database, caching it ...")
                guildSettings = GuildSettings(dbFoundSettings.guild.guildId, dbFoundSettings.prefix, getCommandPattern(dbFoundSettings.prefix, event))
                settings.add(guildSettings)
            }
        }
        logger.info("Returning built cache for the guild ${event.guild.id}.")
        return guildSettings;
    }

    /**
     * Save a modified cache or create a new one wrote into the database.
     * @param guildSettings The settings that need to be saved
     * @param event The event for being able to find all the information such as guild_id and more
     */
    fun saveCache(guildSettings: GuildSettings, event: GuildMessageReceivedEvent){
        // TODO : Simplify this method. Duplicate element so we must EDIT instead of SAVE if found !

        var setting = guildSettingsRepository.findByGuild_GuildId(guildSettings.guildId).orElse(null)
        if(setting == null){
            var guild = guildRepository.findOneByGuildId(guildSettings.guildId).orElse(null)
            if(guild == null){
                var user = userRepository.findOneByUserId(event.author.idLong).orElse(null)
                if(user == null){
                    user = userRepository.save(UserEntity(event.author.idLong, event.author.name, event.author.asTag.split("#")[1]))
                }
                guild = guildRepository.save(GuildEntity(event.guild.idLong, user))
            }
            guildSettingsRepository.save(GuildSettingsEntity(guild, guildSettings.prefix))
        }else{
            guildSettingsRepository.saveGuildSettingsOrUpdateIfNotNull(setting.guild.id, guildSettings.prefix)
        }
        settings.removeAt(settings.indexOf(settings.find { it.guildId == guildSettings.guildId }));
        this.getCachedSettings(event)
    }

    /**
     * Generate a command pattern by building a Regex
     * @param prefix The prefix for which one we want to create this pattern
     * @param event The event for being able to find all the information such as guild_id and more
     */
    fun getCommandPattern(prefix: String, event: GuildMessageReceivedEvent): Regex{
        logger.info("Building command pattern for prefix : $prefix")
        val escapedPrefix: String = Utility.escapeRegex(prefix);
        return Regex("^(<@!?${event.jda.selfUser.id}>\\s+(?:${escapedPrefix}\\s*)?|${escapedPrefix})([^\\s]+)", RegexOption.IGNORE_CASE)
    }

    /**
     * The object of this class
     */
    companion object {
        private val settings: MutableList<GuildSettings> = mutableListOf();
    }
}
