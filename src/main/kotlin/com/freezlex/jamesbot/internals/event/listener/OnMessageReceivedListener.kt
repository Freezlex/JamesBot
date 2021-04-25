package com.freezlex.jamesbot.internals.event.listener

import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.event.DefaultListener
import com.freezlex.jamesbot.internals.models.GuildSettings
import com.freezlex.jamesbot.internals.utils.Utility
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * When a message is received
 */
@Component
class OnMessageReceivedListener @Autowired constructor(
    val guildSettings: GuildSettings
): DefaultListener(){

    /**
     * When a Private guild is received
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if(event.author.isBot)return
        parseMessage(event)
        //TODO : Listener for guilds messages
    }

    /**
     * When a Private message is received
     */
    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        // TODO : Listener for private messages
    }

    /**
     * Build the command pattern to reply to
     * TODO : Change the GuildMessageReceivedEvent to custom event
     * @param event The message event to get all the properties from the guild
     * @return String The command pattern
     */
    private fun parseMessage(event: GuildMessageReceivedEvent): Command?{

        val prefix: String = Utility.escapeRegex(guildSettings.getGuildPrefix(event));
        val regex = Regex("^(<@!?${event.jda.selfUser.id}>\\s+(?:${prefix}[A-z]*)?|${prefix}([A-Z]|\\s))([^\\s]+)", RegexOption.IGNORE_CASE)
        println("Regex : $regex, matches : ${regex.containsMatchIn(event.message.contentRaw)}, in : ${event.message.contentRaw}")
        return null
    }
}

/*if(userRepository.findOneByUserId(event.author.idLong).orElse(null) == null)user = userRepository.save(UserEntity(0, event.author.idLong, userAndTag[0], userAndTag[1]))
        if(guildRepository.findOneByGuildId(event.guild.idLong).orElse(null) == null)guild = guildRepository.save(GuildEntity(0, event.guild.idLong, user))
        if(guildSettingsRepository.findByGuild(guild).orElse(null) == null)guildSettings = guildSettingsRepository.save(GuildSettingsEntity(0, guild, "!!"))*/
