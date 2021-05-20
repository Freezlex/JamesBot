package com.freezlex.jamesbot.implementation.commands.administration

import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.implementation.arguments.MemberType
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User

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
        val user = message.parsed[0].value
        if(user is Member){
            if(message.event.author.id == user.id)throw Exception("Banning yourself doesn't seems to be a good idea")
            message.event.message.reply("Args : ${message.parsed[0].value}").queue()
        }else{
            throw Exception("The arg $user must be a Member")
        }
    }
}
