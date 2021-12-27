package com.freezlex.kohanato.api

import com.freezlex.kohanato.api.events.OnButtonClickEvent
import com.freezlex.kohanato.api.events.OnMessageReceivedEvent
import com.freezlex.kohanato.api.events.OnReadyEvent
import com.freezlex.kohanato.api.events.OnSlashCommandEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener

class Listener: EventListener {

    override fun onEvent(event: GenericEvent) {
        try{
            when(event){
                is ReadyEvent -> OnReadyEvent.run(event)
                is SlashCommandEvent -> OnSlashCommandEvent.run(event)
                is MessageReceivedEvent -> OnMessageReceivedEvent.run(event)
                is ButtonClickEvent -> OnButtonClickEvent.run(event)
            }
        }catch (e: Throwable){
            throw Throwable(e)
        }
    }

    /*fun dispatchSafely(invoker: (ThrowableEvent) -> Unit) {
        try {
            eventListener.forEach(invoker)
            // TODO : Try to fix the invoker ... Idk how to call it ;-;
        } catch (e: Throwable) {
            try {
                eventListener.forEach { it.onInternalError(e)}
            } catch (inner: Throwable) {
                println(inner)
            }
        }
    }*/
}