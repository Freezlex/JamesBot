package com.freezlex.jamesbot.implementation.commands

import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.implementation.arguments.StringType
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.models.GuildCache
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class PrefixCommand: Command(
    "prefix",
    null,
    listOf(
        StringType("prefix")
    ),
    "Set the prefix for the guild",
    false,
    listOf(Permission.ADMINISTRATOR),
    null
) {
    override fun run(message: MessageModel, repositoryManager: RepositoryManager) {
        val guildSettings = message.guildCache.getCachedSettings(message.event)
        if(message.parsed[0].value != "" && guildSettings.prefix != message.parsed[0].value){
            guildSettings.prefix = message.parsed[0].value as String
            message.guildCache.saveCache(guildSettings, message.event)
            message.event.message.reply("Prefix for the guild has been set to ${guildSettings.prefix}").queue()
        }
    }
}