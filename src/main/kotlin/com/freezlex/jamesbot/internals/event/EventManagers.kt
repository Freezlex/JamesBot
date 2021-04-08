package com.freezlex.jamesbot.internals.event

import com.freezlex.jamesbot.internals.commands.CommandsRegistry
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * The event Manager, were all the event are incoming
 */
class EventManagers: ListenerAdapter() {

    /**
     * Method to manage the event broadcast when the bot is ready
     * @param e The ready event received
     */
    override fun onReady(e: ReadyEvent){
        val selfUser = e.jda.selfUser;

        CommandsRegistry().loadCommands();

        println("""
        ||-========================================================
        || Started JamesBot 0.0.1-SNAPSHOT
        || Account Info: ${selfUser.name}#${selfUser.discriminator} (ID: ${selfUser.id})
        || Connected to ${e.jda.guilds.size} guilds, ${e.jda.textChannels.size} text channels
        ||-========================================================
        """.trimMargin("|"))

    }

    /**
     * Method to manage all the incoming messages (DM, Guilds, ...)
     * @param event The global message event received.
     */
    override fun onMessageReceived(event: MessageReceivedEvent) {

        println(event)

        if(event.author.isBot)return;

        val message = event.message.contentRaw;
        CommandsRegistry.getCommandByName(message);


    }
}
