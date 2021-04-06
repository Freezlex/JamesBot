package com.freezlex.jamesbot.managers

import com.freezlex.jamesbot.commands.Command
import com.freezlex.jamesbot.internals.CommandsRegistry
import com.freezlex.jamesbot.utils.Arguments
import com.freezlex.jamesbot.utils.CommandEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventManagers: ListenerAdapter() {

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

    override fun onMessageReceived(event: MessageReceivedEvent) {

        println(event)

        if(event.author.isBot)return;

        CommandsRegistry.getCommandByName("ping")?.run(listOf<Arguments>(), CommandEvent())

        val message = event.message.contentRaw;
        val command = CommandsRegistry.getCommandByName(message);


    }
}
