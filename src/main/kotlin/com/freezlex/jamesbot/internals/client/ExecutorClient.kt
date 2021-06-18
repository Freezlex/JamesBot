package com.freezlex.jamesbot.internals.client

import com.freezlex.jamesbot.internals.exceptions.CommandEvent
import com.freezlex.jamesbot.internals.exceptions.CommandEventAdapter
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.arguments.Snowflake
import com.freezlex.jamesbot.internals.arguments.parser.*
import com.freezlex.jamesbot.internals.commands.CommandRegistry
import com.freezlex.jamesbot.internals.entities.Emoji
import com.freezlex.jamesbot.internals.events.OnMessageReceivedEvent
import com.freezlex.jamesbot.internals.events.OnReadyEvent
import com.freezlex.jamesbot.internals.events.OnSlashCommandEvent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.RawGatewayEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.net.URL

class ExecutorClient(
    packageName: String
): EventListener {
    val commands : CommandRegistry = CommandRegistry()
    val clientCache : ClientCache = ClientCache()
    private val eventListener: MutableList<CommandEventAdapter> = mutableListOf()

    init {
        commands.register(packageName)
        eventListener.add(CommandEvent())
        ArgParser.init
    }

    override fun onEvent(event: GenericEvent){
        try{
            when(event){
                is ReadyEvent -> OnReadyEvent.run(this, event)
                is MessageReceivedEvent -> OnMessageReceivedEvent.run(this, event)
                is SlashCommandEvent -> OnSlashCommandEvent.run(this, event)
            }
        }catch (e: Throwable){
            throw e
        }
    }

    fun dispatchSafely(invoker: (CommandEventAdapter) -> Unit) {
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
    }
}
