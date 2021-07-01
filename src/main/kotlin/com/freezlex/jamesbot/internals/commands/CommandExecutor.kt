package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.CooldownProvider
import kotlin.reflect.KParameter

class CommandExecutor(
    private val executor: ExecutorClient,
    val cmd: CommandFunction,
    val arguments: HashMap<KParameter, Any?>,
    val context: Context
) {

    private val isFromGuild = context.messageContext?.event?.isFromGuild?: context.slashContext!!.event.isFromGuild

    private fun callback(cmd: CommandFunction, context: Context) =
        { success: Boolean, err: Throwable? ->
            if (err != null) {
                val handled = cmd.properties.onCommandError(context, cmd, err)

                if (!handled) {
                    this.executor.dispatchSafely { it.onCommandError(context, cmd, err) }
                }
            }

            this.executor.dispatchSafely { it.onCommandPostInvoke(context, cmd, !success) }
        }

    fun execute(){
        if(!isFromGuild && cmd.properties.isGuildOnly())return

        if(!cmd.properties.isEnabled() && !ClientSettings.getOwners().contains(context.messageContext?.author?.idLong ?: context.slashContext?.author?.idLong))return

        if(cmd.cooldown != null){
            val entityId = when(cmd.cooldown.bucket){
                BucketType.USER -> context.messageContext?.author?.idLong ?: context.slashContext?.author?.idLong
                BucketType.GUILD -> context.messageContext?.guild?.idLong ?: context.slashContext?.guild?.idLong
                BucketType.GLOBAL -> 1
            }

            if(entityId != null){
                if(CooldownProvider.isOnCooldown(entityId, cmd.cooldown.bucket, cmd)){
                    val time = CooldownProvider.getCooldownTime(entityId, cmd.cooldown.bucket, cmd)/1000
                    return executor.dispatchSafely { it.onCommandCooldown(context, cmd, time) }
                }
            }
        }

        if(cmd.properties.isDeveloperOnly() && !ClientSettings.getOwners().contains(context.messageContext?.author?.idLong ?: context.slashContext?.author?.idLong))return

        if(isFromGuild){
            if(!ClientCache.hasPermission(context.invoked, context.messageContext?.author?: context.slashContext!!.author,
                    context.messageContext?.event?.textChannel?: context.slashContext!!.event.textChannel)) return executor.dispatchSafely { it.onUserMissingPermissions(context, cmd, listOf()) }
            if(cmd.properties.userPermissions().isNotEmpty()){
                val userCheck = cmd.properties.userPermissions().filterNot {
                    (context.messageContext?.event?.member?.hasPermission(context.messageContext.event.textChannel, it)?: context.slashContext!!.event.member?.hasPermission(context.slashContext.event.textChannel, it))?:
                    true }
                if(userCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onUserMissingPermissions(context, cmd, userCheck) }
                }
            }
            if(cmd.properties.botPermissions().isNotEmpty()){
                val botCheck = cmd.properties.botPermissions().filterNot {
                    context.messageContext?.event?.guild?.selfMember?.hasPermission(context.messageContext.event.textChannel, it)?: context.slashContext!!.event.guild!!.selfMember.hasPermission(context.slashContext.event.textChannel, it)
                }
                if(botCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onBotMissingPermissions(context, cmd, botCheck) }
                }
            }
        }

        if (cmd.cooldown != null && cmd.cooldown.duration > 0) {
            val entityId = when (cmd.cooldown.bucket) {
                BucketType.USER -> context.messageContext?.author?.idLong ?: context.slashContext?.author?.idLong
                BucketType.GUILD -> context.messageContext?.guild?.idLong ?: context.slashContext?.guild?.idLong
                BucketType.GLOBAL -> -1
            }

            if (entityId != null) {
                val time = cmd.cooldown.timeUnit.toMillis(cmd.cooldown.duration)
                CooldownProvider.setCooldown(entityId, cmd.cooldown.bucket, time, cmd)
            }
        }

        cmd.execute(context, arguments, callback(cmd, context), null)
    }
}
