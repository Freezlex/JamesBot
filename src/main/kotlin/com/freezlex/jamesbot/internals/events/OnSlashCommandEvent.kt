package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.SlashContext
import com.freezlex.jamesbot.internals.api.Utility.findBestMatch
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.commands.CommandFunction
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.CooldownProvider
import com.freezlex.jamesbot.internals.exceptions.BadArgument
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import kotlin.reflect.KParameter

object OnSlashCommandEvent {
    fun run(executor: ExecutorClient, event: SlashCommandEvent){

        // Find the command in the command registry
        val cmd: CommandFunction = if(event.subcommandName != null) {
            executor.commands.filter { it.key.lowercase() == event.subcommandName && it.value.category.category.lowercase() == event.name}.values.firstOrNull()?:
            return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.subcommandName!!,
                findBestMatch(executor.commands.getCommands().map { s -> s.key }, event.subcommandName!!)) }
        }
        else{
            executor.commands[event.name]?: return executor.dispatchSafely { it.onUnknownSlashCommand(event, event.name,  findBestMatch(executor.commands.getCommands().map { s -> s.key }, event.name)) }
        }

        // Create the context for the command
        val context = Context(SlashContext(event), cmd)

        // Check if the command is enabled
        if(!cmd.properties.isEnabled() && !ClientSettings.getOwners().contains(event.user.idLong))return

        // Check if the command is under cooldown
        if(cmd.cooldown != null){
            val entityId = when(cmd.cooldown.bucket){
                BucketType.USER -> context.slashContext!!.author.idLong
                BucketType.GUILD -> context.slashContext!!.guild?.idLong
                BucketType.GLOBAL -> 1
            }

            if(entityId != null){
                if(CooldownProvider.isOnCooldown(entityId, cmd.cooldown.bucket, cmd)){
                    val time = CooldownProvider.getCooldownTime(entityId, cmd.cooldown.bucket, cmd)/1000
                    return executor.dispatchSafely { it.onCommandCooldown(context, cmd, time) }
                }
            }
        }

        // Verify the permissions
        if(cmd.properties.isDeveloperOnly() && !ClientSettings.getOwners().contains(event.user.idLong))return
        if((cmd.properties.isPreview() && ClientSettings.getEarlyUsers().contains(event.user.idLong)) || !ClientSettings.getOwners().contains(event.user.idLong))return executor.dispatchSafely { it.onUserMissingEarlyAccess(context, cmd) }
        if(!event.isFromGuild && cmd.properties.isGuildOnly())return

        if(event.isFromGuild){
            if(cmd.properties.userPermissions().isNotEmpty()){
                val userCheck = cmd.properties.userPermissions().filterNot { event.member?.hasPermission(event.textChannel, it)?: true }
                if(userCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onUserMissingPermissions(context, cmd, userCheck) }
                }
            }
            if(cmd.properties.botPermissions().isNotEmpty()){
                val botCheck = cmd.properties.botPermissions().filterNot { event.guild!!.selfMember.hasPermission(event.textChannel, it) }
                if(botCheck.isNotEmpty()){
                    return executor.dispatchSafely { it.onBotMissingPermissions(context, cmd, botCheck) }
                }
            }
        }

        // Parse the arguments
        val arguments: HashMap<KParameter, Any?>
        try {
            arguments = ArgParser.parseSlashArguments(cmd, context, event.options)
        }catch (e: BadArgument){
            return executor.dispatchSafely { it.onBadArgument(context, cmd, e) }
        }catch (e: Throwable){
            return executor.dispatchSafely { it.onParseError(context, cmd, e) }
        }

        // Validate the command execution
        val cb = { success: Boolean, err: Throwable? ->
            if (err != null) {
                val handled = cmd.properties.onCommandError(context, cmd, err)

                if (!handled) {
                    executor.dispatchSafely { it.onCommandError(context, cmd, err) }
                }
            }

            executor.dispatchSafely { it.onCommandPostInvoke(context, cmd, !success) }
        }

        // Apply the cooldown
        if (cmd.cooldown != null && cmd.cooldown.duration > 0) {
            val entityId = when (cmd.cooldown.bucket) {
                BucketType.USER -> context.slashContext!!.author.idLong
                BucketType.GUILD -> context.slashContext!!.guild?.idLong
                BucketType.GLOBAL -> -1
            }

            if (entityId != null) {
                val time = cmd.cooldown.timeUnit.toMillis(cmd.cooldown.duration)
                CooldownProvider.setCooldown(entityId, cmd.cooldown.bucket, time, cmd)
            }
        }

        // Execute the command
        cmd.execute(context, arguments, cb, null)
    }
}
