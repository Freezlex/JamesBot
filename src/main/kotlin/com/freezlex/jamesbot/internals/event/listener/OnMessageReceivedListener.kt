package com.freezlex.jamesbot.internals.event.listener

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.commands.CommandsRegistry
import com.freezlex.jamesbot.internals.event.DefaultListener
import com.freezlex.jamesbot.internals.models.GuildCache
import com.freezlex.jamesbot.internals.models.GuildSettings
import com.freezlex.jamesbot.internals.utils.Utility
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.log

/**
 * When a message is received
 */
@Component
class OnMessageReceivedListener @Autowired constructor(
    val guildCache: GuildCache,
    val commandsRegistry: CommandsRegistry,
    val repositoryManager: RepositoryManager
): DefaultListener(){

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * When a Private guild is received
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if(event.author.isBot)return
        val isACommand: Boolean = dispatchMessage(event)

        if(!isACommand){
            logger.info("Non-command message received")
        }
        //TODO : Listener for guilds messages
    }

    /**
     * When a Private message is received
     */
    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        // TODO : Listener for private messages
    }

    /**
     * Parse the received message
     * @param event The message event to get all the properties from the guild
     * @return String The command pattern
     */
    private fun dispatchMessage(event: GuildMessageReceivedEvent): Boolean {

        val guildSettings: GuildSettings = guildCache.getCachedSettings(event)
        val parsed = guildSettings.pattern.find(event.message.contentRaw)
        var command: Command? = null
        // Resolving the command by name OR aliases
        if(parsed == null){
            logger.info("Parser is empty")
            return false
        }
        println(parsed.groupValues)
        parsed.groupValues[2].let { command = commandsRegistry.getCommandByName(it) }
        if(command != null){
            logger.info("Command found running it !")
            command!!.execute(event, repositoryManager, parsed)
            return true;
        }
        logger.error("No command found for ${parsed.groupValues[2]}")
        return false
    }
}

/*if(userRepository.findOneByUserId(event.author.idLong).orElse(null) == null)user = userRepository.save(UserEntity(0, event.author.idLong, userAndTag[0], userAndTag[1]))
        if(guildRepository.findOneByGuildId(event.guild.idLong).orElse(null) == null)guild = guildRepository.save(GuildEntity(0, event.guild.idLong, user))
        if(guildSettingsRepository.findByGuild(guild).orElse(null) == null)guildSettings = guildSettingsRepository.save(GuildSettingsEntity(0, guild, "!!"))*/
