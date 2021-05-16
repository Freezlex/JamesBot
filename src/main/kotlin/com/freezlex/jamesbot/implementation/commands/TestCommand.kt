package com.freezlex.jamesbot.implementation.commands

import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.implementation.arguments.IntegerType
import com.freezlex.jamesbot.implementation.arguments.StringType
import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission

/**
 * Implementation for PING command
 */

class TestCommand: Command(
    "test",
    listOf("t"),
    mutableListOf(
        StringType("Premier args mais du texte"),
        IntegerType("First int args", 9, 4 , null),
        IntegerType("Second int args", listOf(8, 10), 8)
    ),
    "This is a Test command",
    true,
    null,
    listOf(Permission.MESSAGE_WRITE),
    hidden = true
) {
    override fun run(message: MessageModel, repositoryManager: RepositoryManager) {
        message.event.message.reply("Parsed args : `${message.parsed.map { n -> n.value }.joinToString("`, `")}`").queue();
    }
}
