package com.freezlex.kohanato.core.events

import com.freezlex.kohanato.core.KohanatoCore
import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.commands.RunCommand
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.commands.parser.Parser
import com.freezlex.kohanato.core.throwable.BadArgument
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import kotlin.reflect.KParameter

object OnUserContextInteractionEvent {
    suspend fun run(executor: KohanatoCore, event: UserContextInteractionEvent){
        val koCommand: KoCommand = if(event.subcommandName != null){
            KoCommands.filter { it.key.equals(event.subcommandName, true) && it.value.category.fName.lowercase() == event.name }.values.firstOrNull()?:
            return executor.dispatchSafely { it.onUnknownCommand(event, event.subcommandName!!) }
        }else{
            KoCommands[event.name]?: return executor.dispatchSafely { it.onUnknownCommand(event, event.name) }
        }

        val arguments: HashMap<KParameter, Any?>
        try{
            arguments = Parser.parseArguments(koCommand, event, event.options)
        }catch (e: BadArgument){
            return executor.dispatchSafely { it.onBadArgument(koCommand, e) }
        }catch (e: Throwable){
            return executor.dispatchSafely { it.onParseError(koCommand, e) }
        }

        RunCommand(executor, event, koCommand, arguments).execute()
    }
}
