package com.freezlex.jamesbot.internals.events

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class OnMessageReceivedEvent {
    companion object{
        fun run(event: MessageReceivedEvent){
            println(event)
        }
    }
}
