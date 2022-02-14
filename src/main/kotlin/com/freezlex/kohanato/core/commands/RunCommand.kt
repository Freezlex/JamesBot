package com.freezlex.kohanato.core.commands

import com.freezlex.kohanato.core.KohanatoCore
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.cooldown.BucketType
import com.freezlex.kohanato.core.cooldown.CooldownProvider
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.reflect.KParameter
import kotlin.time.Duration.Companion.milliseconds

class RunCommand(
    private val executor: KohanatoCore,
    val event: GenericCommandInteractionEvent,
    val koCommand: KoCommand,
    private val arguments: HashMap<KParameter, Any?>,
) {

    private fun callback() =
        { success: Boolean, err: Throwable? ->
            if (err != null) {
                val handled = koCommand.command.onCommandError(this.koCommand, err)
                if (!handled) {
                    this.executor.dispatchSafely { it.onCommandError(this.koCommand, err) }
                }
            }

            this.executor.dispatchSafely { it.onCommandPostInvoke(this.koCommand, !success) }
        }

    suspend fun execute(){
        if(!this.event.isFromGuild && this.koCommand.command.guildOnly)return this.executor.dispatchSafely { it.onGuildOnlyInvoke(this.koCommand) }

        if(koCommand.cooldown != null){
            koCommand.cooldown.forEach {
                val entityId = when(it.bucket){
                    BucketType.USER -> this.event.idLong
                    BucketType.GUILD -> this.event.guild?.idLong
                    BucketType.GLOBAL -> 1
                }

                if(entityId != null){
                    if(CooldownProvider.isOnCooldown(entityId, it.bucket, this.koCommand)){
                        val time = CooldownProvider.getCooldownTime(entityId, it.bucket, this.koCommand)/1000
                        return executor.dispatchSafely { t -> t.onCommandCooldown(this.koCommand, time) }
                    }
                }
            }
        }

        if(this.event.isFromGuild){
            if(koCommand.command.botPermissions.isNotEmpty()){
                val botCheck = koCommand.command.botPermissions.filterNot {
                    this.event.guild!!.selfMember.hasPermission(this.event.textChannel, it)
                }
                if(botCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onBotMissingPermissions(this.koCommand, botCheck) }
                }
            }
        }

        if (koCommand.cooldown != null && koCommand.cooldown.any { it.duration > 0.milliseconds }) {
            koCommand.cooldown.forEach {
                val entityId = when(it.bucket){
                    BucketType.USER -> this.event.idLong
                    BucketType.GUILD -> this.event.guild?.idLong
                    BucketType.GLOBAL -> -1
                }

                if (entityId != null) {
                    val time = it.duration
                    CooldownProvider.setCooldown(entityId, it.bucket, time, this.koCommand)
                }
            }


        }

        koCommand.execute(this.event, arguments, callback(), null)
    }
}