package com.freezlex.jamesbot.internals.api.exceptions

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.CommandFunction
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface CommandEventAdapter {
    fun onBadArgument(ctx: Context, command: CommandFunction, error: BadArgument) {
        println("Bad args")
        error.printStackTrace()
    }

    fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable) {
        println("Cmd error")
        error.printStackTrace()
    }

    fun onCommandPostInvoke(ctx: Context, command: CommandFunction, failed: Boolean) {
        println("Post invoke")
    }

    fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true

    fun onParseError(ctx: Context, command: CommandFunction, error: Throwable) {
        println("Parse error")
        error.printStackTrace()
    }

    fun onInternalError(error: Throwable) {
        println("Internal error")
        error.printStackTrace()
    }

    fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long) {
        ctx.message.reply("The command ${command.name} is under cooldown for the next ${cooldown} {unit time}. Please wait before trying again.")
    }

    fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) {
        ctx.message.reply("Uh ... I'm missing some permission for this action. Missing permissions : `${permissions.map { it.getName() }.joinToString { "${it}, `" }}`")
    }

    fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) {
        ctx.message.reply("Uh ... You are missing some permission for this action. Missing permissions : `${permissions.map { it.getName() }.joinToString { "${it}, `" }}`")
    }

    fun onUnknownCommand(event: MessageReceivedEvent, command: String, args: List<String>) {
        println("Ouais ouais ouais")
        event.message.reply("Hé gros t'as fumé ? Je connais pas la commande  `${command}`")
    }
}
