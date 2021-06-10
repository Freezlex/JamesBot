package com.freezlex.jamesbot.internals.api.exceptions

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.CommandFunction
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class TestImplement: CommandEventAdapter {
    override fun onBadArgument(ctx: Context, command: CommandFunction, error: BadArgument) {
        println("Bad args")
        error.printStackTrace()
    }

    override fun onUnknownCommand(event: MessageReceivedEvent, command: String, args: List<String>) {
        event.message.reply("Hé gros t'as fumé ? Je connais pas la commande  `${command}`")
    }

    override fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable) {
        println("Cmd error")
        error.printStackTrace()
    }

    override fun onCommandPostInvoke(ctx: Context, command: CommandFunction, failed: Boolean) {
        println("Post invoke")
    }

    override fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true

    override fun onParseError(ctx: Context, command: CommandFunction, error: Throwable) {
        println("Parse error")
        error.printStackTrace()
    }

    override fun onInternalError(error: Throwable) {
        println("Internal error")
        error.printStackTrace()
    }

    override fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long) {
        ctx.message.reply("The command ${command.name} is under cooldown for the next ${cooldown} {unit time}. Please wait before trying again.")
    }

    override fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) {
        ctx.message.reply("Uh ... I'm missing some permission for this action. Missing permissions : `${permissions.map { it.getName() }.joinToString { "${it}, `" }}`")
    }

    override fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) {
        ctx.message.reply("Uh ... You are missing some permission for this action. Missing permissions : `${permissions.map { it.getName() }.joinToString { "${it}, `" }}`")
    }
}
