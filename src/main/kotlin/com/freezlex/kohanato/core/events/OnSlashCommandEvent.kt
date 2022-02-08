package com.freezlex.kohanato.core.events

import com.freezlex.kohanato.commands.moderation.BanSlashCommand
import com.freezlex.kohanato.core.KohanatoCore
import com.freezlex.kohanato.core.commands.Commands
import com.freezlex.kohanato.core.commands.RunCommand
import com.freezlex.kohanato.core.commands.contextual.Command
import com.freezlex.kohanato.core.commands.parser.Parser
import com.freezlex.kohanato.core.throwable.BadArgument
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.reflect.KParameter

object OnSlashCommandEvent {
    suspend fun run(executor: KohanatoCore, event: SlashCommandInteractionEvent){

        val command: Command = if(event.subcommandName != null){
            Commands.filter { it.value.command.name.equals(event.subcommandName, true) && it.value.category.lowercase() == event.name }.values.firstOrNull()?:
            return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.subcommandName!!) }
        }else{
            Commands[event.name]?: return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.name) }
        }

        val arguments: HashMap<KParameter, Any?>
        try{
            arguments = Parser.parseArguments(command, event, event.options)
        }catch (e: BadArgument){
            return executor.dispatchSafely { it.onBadArgument(command, e) }
        }catch (e: Throwable){
            return executor.dispatchSafely { it.onParseError(command, e) }
        }

        RunCommand(executor, event, command, arguments)
    }
}