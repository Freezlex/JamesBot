package com.freezlex.kohanato.core.commands.contextual

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent

interface MessageContextCommand : BaseCommand {

    suspend fun run(event: MessageContextInteractionEvent){
        super.run(event)
    }
}
