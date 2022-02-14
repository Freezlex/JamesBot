package com.freezlex.kohanato.core.events

import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.logger
import net.dv8tion.jda.api.events.ReadyEvent

object OnReadyEvent {
    fun run(event: ReadyEvent){
        println("Calling commands sync")
        println(KoCommands
            .syncCommands(event.jda, true)
            .size)

        logger.debug("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")
        println("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")
    }
}