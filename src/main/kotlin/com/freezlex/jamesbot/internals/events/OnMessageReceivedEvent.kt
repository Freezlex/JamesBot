package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.exceptions.BadArgument
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.CooldownProvider
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import kotlin.reflect.KParameter

class OnMessageReceivedEvent {
    companion object{
        fun run(executor: ExecutorClient, event: MessageReceivedEvent){
            if(event.author.isBot || event.isWebhookMessage)return

            val parsed: MatchResult?;
            val args: MutableList<String>
            val command: String
            if(event.isFromGuild){
                parsed = executor.clientCache.getPrefixPattern(event.message).find(event.message.contentRaw) ?: return
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

            val cmd = executor.commands[command]
                ?: executor.commands.findCommandByAlias(command)
                ?: return executor.dispatchSafely { it.onUnknownCommand(event, command, args) }

            val ctx = Context(event, parsed, cmd)

            if(cmd.cooldown != null){
                val entityId = when(cmd.cooldown.bucket){
                    BucketType.USER -> ctx.author.idLong
                    BucketType.GUILD -> ctx.guild?.idLong
                    BucketType.GLOBAL -> 1
                }

                if(entityId != null){
                    if(CooldownProvider.isOnCooldown(entityId, cmd.cooldown.bucket, cmd)){
                        val time = CooldownProvider.getCooldownTime(entityId, cmd.cooldown.bucket, cmd)
                        return executor.dispatchSafely { it.onCommandCooldown(ctx, cmd, time) }
                    }
                }
            }

            if(cmd.properties.developerOnly() && !ClientSettings.owners?.contains(event.author.idLong)!!)return
            if(!event.isFromGuild && cmd.properties.guildOnly())return

            if(event.isFromGuild){
                if(cmd.properties.userPermissions().isNotEmpty()){
                    val userCheck = cmd.properties.userPermissions().filterNot { event.member?.hasPermission(event.textChannel, it)?: true }
                    if(userCheck.isNotEmpty()){
                        return executor.dispatchSafely { it.onUserMissingPermissions(ctx, cmd, userCheck) }
                    }
                }
                if(cmd.properties.botPermissions().isNotEmpty()){
                    val botCheck = cmd.properties.botPermissions().filterNot { event.guild.selfMember.hasPermission(event.textChannel, it) }
                    if(botCheck.isNotEmpty()){
                        return executor.dispatchSafely { it.onBotMissingPermissions(ctx, cmd, botCheck) }
                    }
                }
            }
            val arguments: HashMap<KParameter, Any?>

            try{
                arguments = ArgParser.parseArguments(cmd, ctx, args, ' ')
            }catch (e: BadArgument){
                return executor.dispatchSafely { it.onBadArgument(ctx, cmd, e) }
            }catch (e: Throwable){
                return executor.dispatchSafely { it.onParseError(ctx, cmd, e) }
            }

            val cb = { success: Boolean, err: Throwable? ->
                if (err != null) {
                    val handled = cmd.properties.onCommandError(ctx, cmd, err)

                    if (!handled) {
                        executor.dispatchSafely { it.onCommandError(ctx, cmd, err) }
                    }
                }

                executor.dispatchSafely { it.onCommandPostInvoke(ctx, cmd, !success) }
            }

            if (cmd.cooldown != null && cmd.cooldown.duration > 0) {
                val entityId = when (cmd.cooldown.bucket) {
                    BucketType.USER -> ctx.author.idLong
                    BucketType.GUILD -> ctx.guild?.idLong
                    BucketType.GLOBAL -> -1
                }

                if (entityId != null) {
                    val time = cmd.cooldown.timeUnit.toMillis(cmd.cooldown.duration)
                    CooldownProvider.setCooldown(entityId, cmd.cooldown.bucket, time, cmd)
                }
            }

            cmd.execute(ctx, arguments, cb, null)
        }
    }
}
