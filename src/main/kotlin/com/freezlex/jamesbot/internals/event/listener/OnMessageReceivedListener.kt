package com.freezlex.jamesbot.internals.event.listener

import com.freezlex.jamesbot.internals.event.DefaultListener
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * When a message is received
 */
@Component
class OnMessageReceivedListener @Autowired constructor(): DefaultListener(){

    /**
     * When a Private guild is received
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        println(event.toString())
        //TODO : Listener for guilds messages
    }

    /**
     * When a Private message is received
     */
    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        // TODO : Listener for private messages
    }
}

/*if(userRepository.findOneByUserId(event.author.idLong).orElse(null) == null)user = userRepository.save(UserEntity(0, event.author.idLong, userAndTag[0], userAndTag[1]))
        if(guildRepository.findOneByGuildId(event.guild.idLong).orElse(null) == null)guild = guildRepository.save(GuildEntity(0, event.guild.idLong, user))
        if(guildSettingsRepository.findByGuild(guild).orElse(null) == null)guildSettings = guildSettingsRepository.save(GuildSettingsEntity(0, guild, "!!"))*/
