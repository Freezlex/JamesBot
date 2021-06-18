package com.freezlex.jamesbot.internals.api

import com.freezlex.jamesbot.internals.indexer.Executable
import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction
import java.lang.RuntimeException
import java.util.regex.Pattern

/**
 * The context of the event
 * @param commandContext
 *          The command context if the command is executed with a classic message
 * @param slashContext
 *          The slash context if the command is executed with a slash command
 * @param invoked
 *          The invoked command
 */
class Context(val commandContext: CommandContext?,
              val slashContext: SlashContext?,
              val invoked: Executable){
    constructor(ctx: CommandContext, invoked: Executable): this(ctx, null, invoked)
    constructor(ctx: SlashContext, invoked: Executable): this(null, ctx, invoked)

    fun reply(content: String) {
        if(commandContext != null)commandContext.event.message.reply(content).queue()
        else slashContext?.event?.reply(content)?.queue()
    }

    fun reply(content: MessageEmbed){
        if(commandContext != null)commandContext.event.message.reply(content).queue()
        else slashContext?.event?.channel?.sendMessage(content)?.queue()
    }
}
