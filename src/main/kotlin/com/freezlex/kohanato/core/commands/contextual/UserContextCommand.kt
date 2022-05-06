package com.freezlex.kohanato.core.commands.contextual

import com.freezlex.kohanato.core.KoListener
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent

interface UserContextCommand : BaseCommand {

    suspend fun run(event: UserContextInteractionEvent){
        super.run(event)
    }
}
