package com.freezlex.kohanato.api.events

import com.freezlex.kohanato.logger
import net.dv8tion.jda.api.events.ReadyEvent

object OnReadyEvent {
    fun run(event: ReadyEvent){
        logger.debug("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")
        println("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")
    }
}