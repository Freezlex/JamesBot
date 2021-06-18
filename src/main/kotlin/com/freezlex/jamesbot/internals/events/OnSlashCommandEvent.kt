package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.commands.CommandFunction
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import java.lang.Exception
import kotlin.reflect.KParameter

object OnSlashCommandEvent {
    fun run(executor: ExecutorClient, event: SlashCommandEvent){
        val cmd: CommandFunction = if(event.subcommandName != null) executor.commands[event.subcommandName]?: return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.subcommandName!!) }
        else executor.commands[event.name]?: return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.name) }



        val arguments: HashMap<KParameter, Any?>

        try {
            arguments = ArgParser.parseArguments(cmd, context, args, ' ')
        }catch (e: Exception){
            println(e)
        }

        println("Slash command event received ${event.name}")
    }
}
