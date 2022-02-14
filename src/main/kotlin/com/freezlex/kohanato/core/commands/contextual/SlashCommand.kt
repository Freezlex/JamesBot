package com.freezlex.kohanato.core.commands.contextual

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface SlashCommand: BaseCommand {
    suspend fun run(event: SlashCommandInteractionEvent) {
        super.run(event)
    }
}
