package com.freezlex.jamesbot.internals.events

import net.dv8tion.jda.api.events.ReadyEvent

class OnReadyEvent {
    companion object{
        fun run(event: ReadyEvent){
            println(event)
        }
    }
}
