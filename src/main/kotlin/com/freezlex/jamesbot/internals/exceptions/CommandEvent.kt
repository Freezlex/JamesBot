package com.freezlex.jamesbot.internals.exceptions

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.CommandFunction
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandEvent: CommandEventAdapter {

    override fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable) {
        error.printStackTrace()
    }

    override fun onCommandPostInvoke(ctx: Context, command: CommandFunction, failed: Boolean) {
        if(failed) logger.error("Failed to execute command")
    }

    override fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true

    override fun onParseError(ctx: Context, command: CommandFunction, error: Throwable) {
        error.printStackTrace()
    }

    override fun onInternalError(error: Throwable) {
        error.printStackTrace()
    }

    override fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long) = ctx.reply("The command `${command.name}` is under cooldown. Please wait `$cooldown seconds` before trying again.")

    override fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) = ctx.reply("Uh ... I'm missing some permission for this action. Missing permissions : `${permissions.map { it.getName() }.joinToString { "${it}, `" }}`")

    override fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) = ctx.reply("Uh ... You are missing some permission for this action. ${if(permissions.isNotEmpty())  "Missing permissions : `${permissions.map { it.getName() }.joinToString { ", `" }}`" else ""}")

    override fun onUserMissingEarlyAccess(ctx: Context, command: CommandFunction) = ctx.reply("It seems you're not in the early access program, you aren't allowed to use the `${command.name}` command ! You can still apply to : https://jamesbot.fr/early-access")

    override fun onBadArgument(ctx: Context, cmd: CommandFunction, e: Throwable) = ctx.reply(ctx.language.exception.onBadArgument.format(cmd.name, e.message))

    override fun onUnknownMessageCommand(event: MessageReceivedEvent, command: String, bestMatches: List<String>) = event.message.reply("Unknown command `${command}`. Did you mean `${bestMatches.joinToString("`, `")}` ?").queue()

    override fun onUnknownSlashCommand(event: SlashCommandEvent, command: String, bestMatches: List<String>) = event.reply("Unknown command `${command}`").queue()
}
