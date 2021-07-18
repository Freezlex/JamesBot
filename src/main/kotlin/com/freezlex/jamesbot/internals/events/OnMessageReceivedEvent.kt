package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.MessageContext
import com.freezlex.jamesbot.internals.api.Utility.findBestMatch
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.commands.CommandExecutor
import com.freezlex.jamesbot.internals.exceptions.BadArgument
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.KParameter

object OnMessageReceivedEvent {

    private fun nonCommandEvent(executor: ExecutorClient, event: MessageReceivedEvent){
        if(!event.isFromGuild)return
        val xpCmd = executor.commands.findCommandByName("experience")
            ?: return executor.dispatchSafely { it.onInternalError(Exception("Cannot find the experience command")) }
        if(xpCmd.cmd.isEnabled()){}
    }

    fun run(executor: ExecutorClient, event: MessageReceivedEvent){
        if(Regex("^(Merci\\s(${event.jda.selfUser.name}|<@!?${event.jda.selfUser.idLong}>))", RegexOption.IGNORE_CASE).matches(event.message.contentRaw)) event.message.reply("<:lopotitchat:858994470137888788>").queue()

        if(event.author.isBot || event.isWebhookMessage)return

        val parsed: MatchResult?
        val args: MutableList<String>
        val command: String
        if(event.isFromGuild){
            parsed = ClientCache.getPrefix(event.jda, event.guild).find(event.message.contentRaw) ?: return
            command = parsed.groupValues[2]
            args = event.message.contentRaw.removePrefix(parsed.groupValues[0]).split(" +".toRegex()).toMutableList()
            args.removeIf{ it == "" }
        }else{
            parsed = null
            val trigger = event.message.contentRaw.split(" +".toRegex()).toMutableList()
            trigger.removeIf { it == "" }
            command = trigger.removeAt(0)
            args = trigger
        }

        // if(parsed == null)return nonCommandEvent(executor, event)

        val cmd = executor.commands[command]
            ?: executor.commands.findCommandByAlias(command)
            ?: return executor.dispatchSafely { it.onUnknownMessageCommand(event, command, findBestMatch(executor.commands.getCommands().map { s ->  s.key }, command)) }

        val context = Context(MessageContext(event, parsed), null, cmd)

        if(!ClientCache.checkSubscription(cmd, event.author, event.guild, true)) return executor.dispatchSafely { it.onUserMissingEarlyAccess(context, cmd) }

        val arguments: HashMap<KParameter, Any?>

        try{
            arguments = ArgParser.parseArguments(cmd, context, args, ' ')
        }catch (e: BadArgument){
            return executor.dispatchSafely { it.onBadArgument(context, cmd, e) }
        }catch (e: Throwable){
            return executor.dispatchSafely { it.onParseError(context, cmd, e) }
        }

        CommandExecutor(executor, cmd, arguments, context).execute()
    }
}
