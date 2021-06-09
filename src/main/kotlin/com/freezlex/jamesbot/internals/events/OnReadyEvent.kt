package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.client.ExecutorClient
import net.dv8tion.jda.api.events.ReadyEvent

class OnReadyEvent {
    companion object{
        fun run(executor: ExecutorClient, event: ReadyEvent){
            println(event)
        }
    }
}
