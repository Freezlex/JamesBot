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

    fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long)

    fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>)

    fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>)

    fun onUserMissingEarlyAccess(ctx: Context, command: CommandFunction)

    fun onBadArgument(ctx: Context, cmd: CommandFunction, e: Throwable)

    fun onUnknownMessageCommand(event: MessageReceivedEvent, command: String, bestMatches: List<String>)

    fun onUnknownSlashCommand(event: SlashCommandEvent, command: String, bestMatches: List<String>)
}
