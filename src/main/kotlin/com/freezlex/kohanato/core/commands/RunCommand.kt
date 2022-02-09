package com.freezlex.kohanato.core.commands

import com.freezlex.kohanato.core.KohanatoCore
import com.freezlex.kohanato.core.commands.contextual.Command
import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import com.freezlex.kohanato.core.cooldown.BucketType
import com.freezlex.kohanato.core.cooldown.CooldownProvider
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.reflect.KParameter
import kotlin.time.Duration.Companion.milliseconds

class RunCommand(
    private val executor: KohanatoCore,
    val event: SlashCommandInteractionEvent,
    val command: Command,
    private val arguments: HashMap<KParameter, Any?>,
) {

    private fun callback() =
        { success: Boolean, err: Throwable? ->
            if (err != null) {
                val handled = command.command.onCommandError(this.command, err)
                if (!handled) {
                    this.executor.dispatchSafely { it.onCommandError(this.command, err) }
                }
            }

            this.executor.dispatchSafely { it.onCommandPostInvoke(this.command, !success) }
        }

    suspend fun execute(){
        if(!this.event.isFromGuild && this.command.command.guildOnly)return this.executor.dispatchSafely { it.onGuildOnlyInvoke(this.command) }

        if(command.cooldown != null){
            command.cooldown.forEach {
                val entityId = when(it.bucket){
                    BucketType.USER -> this.event.idLong
                    BucketType.GUILD -> this.event.guild?.idLong
                    BucketType.GLOBAL -> 1
                }

                if(entityId != null){
                    if(CooldownProvider.isOnCooldown(entityId, it.bucket, this.command)){
                        val time = CooldownProvider.getCooldownTime(entityId, it.bucket, this.command)/1000
                        return executor.dispatchSafely { t -> t.onCommandCooldown(this.command, time) }
                    }
                }
            }
        }

        if(this.event.isFromGuild){
            if(command.command.botPermissions.isNotEmpty()){
                val botCheck = command.command.botPermissions.filterNot {
                    this.event.guild!!.selfMember.hasPermission(this.event.textChannel, it)
                }
                if(botCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onBotMissingPermissions(this.command, botCheck) }
                }
            }
        }

        if (command.cooldown != null && command.cooldown.any { it.duration > 0.milliseconds }) {
            command.cooldown.forEach {
                val entityId = when(it.bucket){
                    BucketType.USER -> this.event.idLong
                    BucketType.GUILD -> this.event.guild?.idLong
                    BucketType.GLOBAL -> -1
                }

                if (entityId != null) {
                    val time = it.duration
                    CooldownProvider.setCooldown(entityId, it.bucket, time, this.command)
                }
            }


        }

        command.execute(this.event, arguments, callback(), null)
    }
}