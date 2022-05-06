package com.freezlex.kohanato.core.events

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.logger
import net.dv8tion.jda.api.events.ReadyEvent

object OnReadyEvent {
    fun run(kl: KoListener){
        println("Calling commands sync")
        println(KoCommands
            .syncCommands(kl.event.jda, true)
            .size)

        logger.debug("${kl.event.jda.selfUser.name} is ready (id: ${kl.event.jda.selfUser.id})")
        println("${kl.event.jda.selfUser.name} is ready (id: ${kl.event.jda.selfUser.id})")
    }
}