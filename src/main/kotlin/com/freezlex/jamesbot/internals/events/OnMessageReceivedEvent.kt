package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.api.MessageContext
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.Utility.findBestMatch
import com.freezlex.jamesbot.internals.exceptions.BadArgument
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.CooldownProvider
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import kotlin.reflect.KParameter

object OnMessageReceivedEvent {

    private fun nonCommandEvent(executor: ExecutorClient, event: MessageReceivedEvent){
        if(!event.isFromGuild)return
        val xpCmd = executor.commands.findCommandByName("experience")
            ?: return executor.dispatchSafely { it.onInternalError(Exception("Cannot find the experience command")) }
        if(xpCmd.cmd.isEnabled()){}
    }

    fun run(executor: ExecutorClient, event: MessageReceivedEvent){
        if(event.author.isBot || event.isWebhookMessage)return

        val parsed: MatchResult?
        val args: MutableList<String>
        val command: String
        if(event.isFromGuild){
            parsed = ClientCache.getPrefixPattern(event.message).find(event.message.contentRaw) ?: return
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

        if(!cmd.properties.isEnabled() && !ClientSettings.getOwners().contains(event.author.idLong))return

        if(cmd.cooldown != null){
            val entityId = when(cmd.cooldown.bucket){
                BucketType.USER -> context.messageContext!!.author.idLong
                BucketType.GUILD -> context.messageContext!!.guild?.idLong
                BucketType.GLOBAL -> 1
            }

            if(entityId != null){
                if(CooldownProvider.isOnCooldown(entityId, cmd.cooldown.bucket, cmd)){
                    val time = CooldownProvider.getCooldownTime(entityId, cmd.cooldown.bucket, cmd)/1000
                    return executor.dispatchSafely { it.onCommandCooldown(context, cmd, time) }
                }
            }
        }

        if(cmd.properties.isDeveloperOnly() && !ClientSettings.getOwners().contains(event.author.idLong))return
        if((cmd.properties.isPreview() && ClientSettings.getEarlyUsers().contains(event.author.idLong)) || !ClientSettings.getOwners().contains(event.author.idLong))return executor.dispatchSafely { it.onUserMissingEarlyAccess(context, cmd) }
        if(!event.isFromGuild && cmd.properties.isGuildOnly())return

        if(event.isFromGuild){
            if(cmd.properties.userPermissions().isNotEmpty()){
                val userCheck = cmd.properties.userPermissions().filterNot { event.member?.hasPermission(event.textChannel, it)?: true }
                if(userCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onUserMissingPermissions(context, cmd, userCheck) }
                }
            }
            if(cmd.properties.botPermissions().isNotEmpty()){
                val botCheck = cmd.properties.botPermissions().filterNot { event.guild.selfMember.hasPermission(event.textChannel, it) }
                if(botCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onBotMissingPermissions(context, cmd, botCheck) }
                }
            }
        }
        val arguments: HashMap<KParameter, Any?>

        try{
            arguments = ArgParser.parseArguments(cmd, context, args, ' ')
        }catch (e: BadArgument){
            return executor.dispatchSafely { it.onBadArgument(context, cmd, e) }
        }catch (e: Throwable){
            return executor.dispatchSafely { it.onParseError(context, cmd, e) }
        }

        val cb = { success: Boolean, err: Throwable? ->
            if (err != null) {
                val handled = cmd.properties.onCommandError(context, cmd, err)

                if (!handled) {
                    executor.dispatchSafely { it.onCommandError(context, cmd, err) }
                }
            }

            executor.dispatchSafely { it.onCommandPostInvoke(context, cmd, !success) }
        }

        if (cmd.cooldown != null && cmd.cooldown.duration > 0) {
            val entityId = when (cmd.cooldown.bucket) {
                BucketType.USER -> context.messageContext!!.author.idLong
                BucketType.GUILD -> context.messageContext!!.guild?.idLong
                BucketType.GLOBAL -> -1
            }

            if (entityId != null) {
                val time = cmd.cooldown.timeUnit.toMillis(cmd.cooldown.duration)
                CooldownProvider.setCooldown(entityId, cmd.cooldown.bucket, time, cmd)
            }
        }

        cmd.execute(context, arguments, cb, null)
    }
}
