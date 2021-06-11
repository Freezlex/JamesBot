package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.events.ReadyEvent

class OnReadyEvent {
    companion object{
        fun run(executor: ExecutorClient, event: ReadyEvent){
            logger.debug("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")
        }
    }
}
