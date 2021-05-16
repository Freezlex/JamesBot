package com.freezlex.jamesbot.implementation.commands.administration

import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.implementation.arguments.MemberType
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission

class BanCommand: Command(
    "ban",
    null,
    mutableListOf(
        MemberType("Member", true)
    ),
    "Set the prefix for the guild",
    false,
    listOf(Permission.ADMINISTRATOR),
    null
) {
    override fun run(message: MessageModel, repositoryManager: RepositoryManager) {
        if(message.event.author == message.parsed[0].value)throw Exception("Banning yourself doesn't seems to bee a good idea")
        message.event.message.reply("Args : ${message.parsed[0].value}").queue()
    }
}
