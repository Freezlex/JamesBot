package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.SlashContext
import com.freezlex.jamesbot.internals.api.Utility.findBestMatch
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.commands.CommandExecutor
import com.freezlex.jamesbot.internals.commands.CommandFunction
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.CooldownProvider
import com.freezlex.jamesbot.internals.exceptions.BadArgument
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import kotlin.reflect.KParameter

object OnSlashCommandEvent {
    fun run(executor: ExecutorClient, event: SlashCommandEvent){



        // Find the command in the command registry
        val cmd: CommandFunction = if(event.subcommandName != null) {
            executor.commands.filter { it.value.cmd.name().equals(event.subcommandName, true) && it.value.category.category.lowercase() == event.name}.values.firstOrNull()?:
            return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.subcommandName!!,
                findBestMatch(executor.commands.getCommands().map { s -> s.key }, event.subcommandName!!)) }
        }
        else{
            executor.commands[event.name]?: return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.name,  findBestMatch(executor.commands.getCommands().map { s -> s.key }, event.name)) }
        }

        // Create the context for the command
        val context = Context(SlashContext(event), cmd)
        println(!ClientSettings.getEarlyUsers().contains(event.user.idLong))
        println(!ClientSettings.getEarlyUsers().contains(event.guild?.ownerIdLong))

        if(!ClientSettings.getEarlyUsers().contains(event.user.idLong) && !ClientSettings.getEarlyUsers().contains(event.guild?.ownerIdLong)) return executor.dispatchSafely { it.onUserMissingEarlyAccess(context, cmd) }

        // Parse the arguments
        val arguments: HashMap<KParameter, Any?>
        try {
            arguments = ArgParser.parseSlashArguments(cmd, context, event.options)
        }catch (e: BadArgument){
            return executor.dispatchSafely { it.onBadArgument(context, cmd, e) }
        }catch (e: Throwable){
            return executor.dispatchSafely { it.onParseError(context, cmd, e) }
        }

        CommandExecutor(executor, cmd, arguments, context).execute()
    }
}
