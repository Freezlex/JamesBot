package com.freezlex.jamesbot.commands.test

import com.freezlex.jamesbot.api.contextual.SlashCommand
import com.freezlex.jamesbot.api.contextual.TextualCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class TestTextualCommand: TextualCommand {
    override val category: String = "TEST"

    override suspend fun run(event: MessageReceivedEvent) {
        super.run(event)
    }
}

class TestSlashCommand: SlashCommand {
    override val category: String = "TEST"

    override suspend fun run(event: SlashCommandEvent) {
        super.run(event)
    }
}