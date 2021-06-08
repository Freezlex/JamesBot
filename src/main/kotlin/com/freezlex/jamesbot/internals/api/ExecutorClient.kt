package com.freezlex.jamesbot.internals.api

import com.freezlex.jamesbot.internals.events.OnMessageReceivedEvent
import com.freezlex.jamesbot.internals.events.OnReadyEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.*

class ExecutorClient: EventListener {

    override fun onEvent(event: GenericEvent){
        try{
            when(event){
                is ReadyEvent -> OnReadyEvent.run(event)
                is MessageReceivedEvent -> OnMessageReceivedEvent.run(event)
            }
        }catch (e: Throwable){

        }
    }
}