package com.freezlex.kohanato.core.commands.contextual

import com.freezlex.kohanato.core.KoListener
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent

interface MessageContextCommand : BaseCommand {

    suspend fun run(kl: KoListener, event: MessageContextInteractionEvent){
        super.run(kl, event)
    }
}
