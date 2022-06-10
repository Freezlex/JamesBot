package com.freezlex.kohanato.implementation.test

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.Categories
import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class TestSlashCommand: SlashCommand {

    override val name: String
        get() = "test"
    override val category: Categories = Categories.UNCATEGORIZED
    override val isSubGroup: Boolean
        get() = false

    override suspend fun run(core:KoListener, event: SlashCommandInteractionEvent) {
        super.run(core, event)
    }
}
