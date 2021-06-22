package com.freezlex.jamesbot.implementation.moderation

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.util.concurrent.TimeUnit

class BanCommand: Cmd {
    override fun name(): String = "Ban"
    override fun category(): CommandCategory = CommandCategory.MODERATION
    override fun description(): String = "Ban a user from the guild"
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun userPermissions() = listOf(Permission.BAN_MEMBERS)
    override fun isGuildOnly() = true

    fun run(ctx: Context, @Argument(type = OptionType.USER) member: Member
    ) {
        ctx.reply("You are about to ban ${member.user.name}")
    }
}
