package com.freezlex.jamesbot.internals.exceptions

import com.freezlex.jamesbot.internals.api.CommandContext
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.CommandFunction
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface CommandEventAdapter {

    fun onCommandError(ctx: CommandContext, command: CommandFunction, error: Throwable)

    fun onCommandPostInvoke(ctx: CommandContext, command: CommandFunction, failed: Boolean)

    fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true

    fun onParseError(ctx: CommandContext, command: CommandFunction, error: Throwable)

    fun onInternalError(error: Throwable)

    fun onCommandCooldown(ctx: CommandContext, command: CommandFunction, cooldown: Long)

    fun onBotMissingPermissions(ctx: CommandContext, command: CommandFunction, permissions: List<Permission>)

    fun onUserMissingPermissions(ctx: CommandContext, command: CommandFunction, permissions: List<Permission>)

    fun onUserMissingEarlyAccess(ctx: CommandContext, command: CommandFunction)

    fun onUnknownCommand(event: MessageReceivedEvent, command: String, args: List<String>)

    fun onBadArgument(ctx: CommandContext, cmd: CommandFunction, e: Throwable)

    fun onUnknownSlashCommand(event: SlashCommandEvent, command: String)
}
