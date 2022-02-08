package com.freezlex.kohanato.core.throwable

import com.freezlex.kohanato.core.commands.Commands
import com.freezlex.kohanato.core.commands.contextual.Command
import com.freezlex.kohanato.core.extensions.findBestMatch
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object CommandThrowable {

    fun onInternalError(error: Throwable)  = error.printStackTrace()

    fun onCommandError(command: Command, error: Throwable) = error.printStackTrace()

    fun onBadArgument(command: Command, e: BadArgument){}

    fun onParseError(command: Command, e: Throwable){}

    fun onCommandPostInvoke(command: Command, failed: Boolean){}

    fun onCommandPreInvoke(command: Command) = true

    fun onGuildOnlyInvoke(command: Command){}

    fun onCommandCooldown(command: Command, cooldown: Long){}

    fun onBotMissingPermissions(command: Command, permission: List<Permission>){}

    /**
     * Message : "Unknown interaction `%1$s`, Did you mean `%2$s` ?"
     * @param event The event of the slash event
     * @param command The command sent by the user
     * @param bestMatches A list of close match from what the user sent
     */
    fun onUnknownSlashCommand(event: SlashCommandInteractionEvent, tentative: String){
        event.reply("Commande `$tentative` inconnue. Vouliez-vous dire `${findBestMatch(Commands.map { s -> s.key }, tentative)}` ?")
    }
}