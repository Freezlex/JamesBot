package com.freezlex.jamesbot.internals.event.listener

import com.freezlex.jamesbot.database.entity.UserSettingsEntity
import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.commands.CommandsRegistry
import com.freezlex.jamesbot.internals.event.DefaultListener
import com.freezlex.jamesbot.internals.models.GuildCache
import com.freezlex.jamesbot.internals.models.GuildSettings
import com.freezlex.jamesbot.internals.models.MessageModel
import com.freezlex.jamesbot.internals.models.UserCache
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * When a message is received
 */
@Component
class OnMessageReceivedListener @Autowired constructor(
    val guildCache: GuildCache,
    val userCache: UserCache,
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

        /*val userCache: UserSettingsEntity? = userCache.getCache(event)
        if(userCache != null){

        }else {

        }*/
        val guildSettings: GuildSettings = guildCache.getCachedSettings(event)
        val parsed = guildSettings.pattern.find(event.message.contentRaw)
        val command: Command?
        if(parsed == null){
            logger.info("No argument parsed")
            return false
        }
        parsed.groupValues[2].let { command = commandsRegistry.getCommandByName(it) }
        if(command != null){
            logger.info("Running de command '${command.name}'")
            try{
                // TODO : Create parser
                val parsedString = event.message.contentRaw.removePrefix(parsed.groupValues[0])
                command.execute(MessageModel(parsed, event, null, guildCache , null, parsedString, parsedString.split(" ").toMutableList()), repositoryManager)
            }catch (e: Exception){
                e.message?.let { event.message.reply(":x: $it").queue() }
            }
            return true
        }
        logger.info("No command found for ${parsed.groupValues[2]}")
        return false
    }
}
