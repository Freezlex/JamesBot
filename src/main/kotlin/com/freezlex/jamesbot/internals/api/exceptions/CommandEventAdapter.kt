package com.freezlex.jamesbot.internals.api.exceptions

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.CommandFunction
import net.dv8tion.jda.api.Permission
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

    fun onUnknownCommand(event: MessageReceivedEvent, command: String, args: List<String>)

    fun onBadArgument(ctx: Context, cmd: CommandFunction, e: Throwable)
}
