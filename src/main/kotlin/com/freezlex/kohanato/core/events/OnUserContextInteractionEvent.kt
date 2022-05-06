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
    suspend fun run(core: KoListener){

        if(core.event !is UserContextInteractionEvent) return core.dispatchSafely {  }
        val koCommand: KoCommand = if(core.event.subcommandName != null){
            KoCommands.filter { it.key.equals(core.event.subcommandName, true) && it.value.category.fName.lowercase() == core.event.name }.values.firstOrNull()?:
            return core.dispatchSafely { it.onUnknownCommand(core.event, core.event.subcommandName!!) }
        }else{
            KoCommands[core.event.name]?: return core.dispatchSafely { it.onUnknownCommand(core.event, core.event.name) }
        }

        val arguments: HashMap<KParameter, Any?>
        try{
            arguments = Parser.parseArguments(koCommand, core.event, core.event.options)
        }catch (e: BadArgument){
            return core.dispatchSafely { it.onBadArgument(koCommand, e) }
        }catch (e: Throwable){
            return core.dispatchSafely { it.onParseError(koCommand, e) }
        }

        RunCommand(core, core.event, koCommand, arguments).execute()
    }
}
