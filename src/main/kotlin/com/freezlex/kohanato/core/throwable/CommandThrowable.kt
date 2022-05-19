package com.freezlex.kohanato.core.throwable

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.extensions.findBestMatch
import com.freezlex.kohanato.core.i18n.LangKey
import com.freezlex.kohanato.core.logger
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.Interaction

object CommandThrowable {

    fun onInternalError(error: Throwable)  = error.printStackTrace()

    fun onCommandError(core: KoListener, command: KoCommand, error: Throwable) = error.printStackTrace()

    fun onBadArgument(core: KoListener, command: KoCommand, e: BadArgument){
        this.replyEvent(core.event,
            core.language.getString(
                this,
                "CommandThrowable",
                "onBadArgument",
                "Bad argument provided"))
    }

    fun onParseError(core: KoListener, command: KoCommand, e: Throwable){
        println("Parsing error ! $e")
    }

    fun onCommandPostInvoke(core: KoListener, command: KoCommand, failed: Boolean){
        println("Error at post invoke")
    }

    fun onCommandPreInvoke(core: KoListener, command: KoCommand) {
        this.replyEvent(core.event,
            core.language.getString(
                this,
                "CommandThrowable",
                "onCommandPreInvoke",
                "Command Post Invoke"))
    }

    fun onGuildOnlyInvoke(core: KoListener, command: KoCommand){
        println("Is Guild only")
    }

    fun onCommandCooldown(core: KoListener, command: KoCommand, cooldown: Long){
        println("On cooldown")
    }

    fun onBotMissingPermissions(core: KoListener, command: KoCommand, permission: List<Permission>){
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

    private fun replyEvent(event: GenericEvent, string: String){
        when(event) {
            is GenericCommandInteractionEvent -> event.reply(string).setEphemeral(true).queue()
            is GenericContextInteractionEvent<*> -> event.reply(string).setEphemeral(true).queue()
            is MessageReceivedEvent -> event.message.reply(string).queue()
            else -> logger.warn(string)
        }
    }
}