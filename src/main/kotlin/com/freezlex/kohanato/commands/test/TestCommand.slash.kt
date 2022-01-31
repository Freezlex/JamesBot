package com.freezlex.kohanato.commands.test

import com.freezlex.kohanato.api.contextual.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class TestSlashCommand: SlashCommand {
    override val category: String = "TEST"
    override val subSlashCommand: Boolean = false

    override suspend fun run(event: SlashCommandEvent) {
        super.run(event)
    }
}
