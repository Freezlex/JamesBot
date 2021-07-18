package com.freezlex.jamesbot.implementation.settings

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.util.concurrent.TimeUnit

class PrefixCommand: Cmd {
    override fun name(): String = "Prefix"
    override fun category(): CommandCategory = CommandCategory.SETTINGS
    override fun description(): String = "Change the prefix for the bot on this guild"
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun userPermissions() = listOf(Permission.ADMINISTRATOR)
    override fun isGuildOnly(): Boolean = true

    fun run(ctx: Context, @Argument(type = OptionType.STRING) prefix: String?){
        if(prefix != null)definePrefix(ctx, prefix)
        else showPrefix(ctx)
    }

    fun showPrefix(ctx: Context){
        val guildSettings = ClientCache.getGuildSettingsOrCreate(ctx.messageContext?.guild?: ctx.slashContext!!.guild!!)

    }

    fun definePrefix(ctx: Context, prefix:  String){

    }

}
