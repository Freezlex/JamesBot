package com.freezlex.jamesbot.internals.exceptions

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.CommandFunction
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface CommandEventAdapter {

    fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable)

    fun onCommandPostInvoke(ctx: Context, command: CommandFunction, failed: Boolean)

    fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true

    fun onParseError(ctx: Context, command: CommandFunction, error: Throwable)

    fun onInternalError(error: Throwable)

    /**
     * Message : "The command `%1$s` is under cooldown. Please wait `%2$d seconds` before trying again."
     * @param ctx The context of the message
     * @param command The command which is in cooldown
     * @param cooldown The time of the cooldown
     */
    fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long)

    /**
     * Message : "Uh ... I'm missing some permission for this action. Missing permissions : `%$s`"
     * @param ctx The context of the message
     * @param command The command for which one the bot is missing permission
     * @param permissions The list of the missing permissions
     */
    fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>)

    /**
     * Message : "Uh ... You are missing some permission for this action.%$s"
     * @param ctx The context of the message
     * @param command The command for which one the user is missing permission
     * @param permissions The list of the missing permissions
     */
    fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>)

    /**
     * Message : "It seems you're not in the early access program, you aren't allowed to use the `%1$s` command !"
     * @param ctx The context of the message
     * @param command The command for which one the user is missing the early access program
     */
    fun onUserMissingEarlyAccess(ctx: Context, command: CommandFunction)

    /**
     * Message : "You provided an invalid argument for the %1$s command.%2$s"
     * @param ctx The context of the message
     * @param cmd The command for which one the argument has failed
     * @param e The throw of the error that define which argument isn't valid
     */
    fun onBadArgument(ctx: Context, cmd: CommandFunction, e: Throwable)

    /**
     * Message : "Unknown command `%1$s`. Did you mean `%2$s` ?"
     * @param event The event of the message event
     * @param command The command sent by the user
     * @param bestMatches A list of close match from what the user sent
     */
    fun onUnknownMessageCommand(event: MessageReceivedEvent, command: String, bestMatches: List<String>)

    /**
     * Message : "Unknown interaction `%1$s`, Did you mean `%2$s` ?"
     * @param event The event of the slash event
     * @param command The command sent by the user
     * @param bestMatches A list of close match from what the user sent
     */
    fun onUnknownSlashCommand(event: SlashCommandEvent, command: String, bestMatches: List<String>)
}
