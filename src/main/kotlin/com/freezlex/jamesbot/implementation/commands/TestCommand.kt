package com.freezlex.jamesbot.implementation.commands

import com.freezlex.jamesbot.database.entity.UserEntity
import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.implementation.arguments.IntegerType
import com.freezlex.jamesbot.implementation.arguments.StringType
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.springframework.stereotype.Component

/**
 * Implementation for PING command
 */

class TestCommand: Command(
    "test",
    listOf("t"),
    listOf(
        StringType("First string args" ,0, null, null),
        IntegerType("First int args", 9, 4, null),
        IntegerType("Second int args", listOf(8, 10), null),
    ),
    "This is a Test command",
    true,
    null,
    listOf(Permission.MESSAGE_WRITE)
) {
    override fun run(message: MessageModel, repositoryManager: RepositoryManager) {
        println("Salut mon pote")
        // repositoryManager.userRepository.save(UserEntity(0, 306703362261254154, "Freezlex", "0001"))
    }
}
