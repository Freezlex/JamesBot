package com.freezlex.jamesbot.internals.exceptions

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.commands.CommandFunction
import com.freezlex.jamesbot.internals.i18n.LanguageList
import com.freezlex.jamesbot.internals.i18n.LanguageModel
import com.freezlex.jamesbot.internals.i18n.Languages
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandEvent: CommandEventAdapter {

    override fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable) = error.printStackTrace()

    override fun onCommandPostInvoke(ctx: Context, command: CommandFunction, failed: Boolean) = if(failed) logger.error("Failed to execute command ${command.name}") else logger.debug("${command.name} command executed")

    override fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true

    override fun onParseError(ctx: Context, command: CommandFunction, error: Throwable) = error.printStackTrace()

    override fun onInternalError(error: Throwable)  = error.printStackTrace()

    override fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long) = ctx.reply(ctx.language.exception.onCommandCooldown.format(command.name, cooldown))

    override fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) = ctx.reply(ctx.language.exception.onBotMissingPermission.format(permissions.map { it.getName() }.joinToString { "`, `" }))

    override fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) = ctx.reply(ctx.language.exception.onUserMissingPermission.format(if(permissions.isNotEmpty())  " Missing permissions : `${permissions.map { it.getName() }.joinToString { "`, `" }}`" else ""))

    override fun onUserMissingEarlyAccess(ctx: Context, command: CommandFunction) = ctx.reply(ctx.language.exception.onUserMissingEarlyAccess.format(command.name))

    override fun onBadArgument(ctx: Context, cmd: CommandFunction, e: Throwable) = ctx.reply(ctx.language.exception.onBadArgument.format(cmd.name, e.message))

    override fun onUnknownMessageCommand(event: MessageReceivedEvent, command: String, bestMatches: List<String>) {
        val language: LanguageModel = ClientCache.resolveLanguage(event.author, event.guild)
        event.message.reply(language.exception.onUnknownMessageCommand.format(command, bestMatches.joinToString("`, `"))).queue()
    }

    override fun onUnknownSlashCommand(event: SlashCommandEvent, command: String, bestMatches: List<String>) {
        val language: LanguageModel = ClientCache.resolveLanguage(event.user, event.guild)
        event.reply(language.exception.onUnknownSlashCommand.format(command, bestMatches.joinToString("`, `"))).queue()
    }
}
