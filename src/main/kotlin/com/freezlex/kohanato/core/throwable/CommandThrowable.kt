package com.freezlex.kohanato.core.throwable

import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.extensions.findBestMatch
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object CommandThrowable {

    fun onInternalError(error: Throwable)  = error.printStackTrace()

    fun onCommandError(koCommand: KoCommand, error: Throwable) = error.printStackTrace()

    fun onBadArgument(koCommand: KoCommand, e: BadArgument){
        println("Bad args")
    }

    fun onParseError(koCommand: KoCommand, e: Throwable){
        println("Parsing error ! ${e}")
    }

    fun onCommandPostInvoke(koCommand: KoCommand, failed: Boolean){
        println("Error at post invoke")
    }

    fun onCommandPreInvoke(koCommand: KoCommand) = true

    fun onGuildOnlyInvoke(koCommand: KoCommand){
        println("Is Guild only")
    }

    fun onCommandCooldown(koCommand: KoCommand, cooldown: Long){
        println("On cooldown")
    }

    fun onBotMissingPermissions(koCommand: KoCommand, permission: List<Permission>){
        println("I'm cooling down")
    }

    /**
     * Message : "Unknown interaction `%1$s`, Did you mean `%2$s` ?"
     * @param event The event of the slash event
     * @param command The command sent by the user
     * @param bestMatches A list of close match from what the user sent
     */
    fun onUnknownCommand(event: GenericCommandInteractionEvent, tentative: String){
        event.reply("Commande `$tentative` inconnue. Vouliez-vous dire `${findBestMatch(KoCommands.map { s -> s.key }, tentative)}` ?").queue()
    }
}