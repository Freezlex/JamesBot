package com.freezlex.kohanato.core.events

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.KohanatoCore
import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.commands.RunCommand
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.commands.parser.Parser
import com.freezlex.kohanato.core.throwable.BadArgument
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import kotlin.reflect.KParameter

object OnUserContextInteractionEvent {
    suspend fun run(event: UserContextInteractionEvent, core: KoListener){

        val koCommand: KoCommand = if(event.subcommandName != null){
            KoCommands.filter { it.key.equals(event.subcommandName, true) && it.value.category.fName.lowercase() == event.name }.values.firstOrNull()?:
            return core.dispatchSafely { it.onUnknownCommand(event, event.subcommandName!!) }
        }else{
            KoCommands[event.name]?: return core.dispatchSafely { it.onUnknownCommand(event, event.name) }
        }

        val arguments: HashMap<KParameter, Any?>
        try{
            arguments = Parser.parseArguments(koCommand, event, event.options)
        }catch (e: BadArgument){
            return core.dispatchSafely { it.onBadArgument(core, koCommand, e) }
        }catch (e: Throwable){
            return core.dispatchSafely { it.onParseError(core, koCommand, e) }
        }

        RunCommand(core, event, koCommand, arguments).execute()
    }
}
