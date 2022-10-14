package com.freezlex.kohanato.core.commands

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.cooldown.BucketType
import com.freezlex.kohanato.core.cooldown.CooldownProvider
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import kotlin.reflect.KParameter
import kotlin.time.Duration.Companion.milliseconds

class RunCommand(
    private val core: KoListener,
    val event: GenericCommandInteractionEvent,
    val koCommand: KoCommand,
    private val arguments: HashMap<KParameter, Any?>,
) {

    private fun callback() =
        { success: Boolean, err: Throwable? ->
            if (err != null) {
                val handled = koCommand.command.onCommandError(this.koCommand, err)
                if (!handled) {
                    this.core.dispatchSafely { it.onCommandError(core, this.koCommand, err) }
                }
            }

            this.core.dispatchSafely { it.onCommandPostInvoke(core, this.koCommand, !success) }
        }

    suspend fun execute(){
        if(!this.event.isFromGuild && this.koCommand.command.guildOnly)return this.core.dispatchSafely { it.onGuildOnlyInvoke(core, this.koCommand) }

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
                        return core.dispatchSafely { t -> t.onCommandCooldown(core, this.koCommand, time) }
                    }
                }
            }
        }

        if(this.event.isFromGuild){
            if(koCommand.command.botPermissions.isNotEmpty()){
                val botCheck = koCommand.command.botPermissions.filterNot {
                    this.event.guild!!.selfMember.hasPermission(this.event.guildChannel, it)
                }
                if(botCheck.isNotEmpty()){
                    return core.dispatchSafely { it.onBotMissingPermissions(core, this.koCommand, botCheck) }
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

        koCommand.execute(event, this.core, arguments, callback(), null)
    }
}