package com.freezlex.kohanato.commands.test

import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TestSlashCommand: SlashCommand {
    override val category: String = "TEST"
    override val subSlashCommand: Boolean = false

    override suspend fun run(event: SlashCommandInteractionEvent) {
        super.run(event)
    }
}
