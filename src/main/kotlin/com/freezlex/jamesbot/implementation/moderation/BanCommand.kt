package com.freezlex.jamesbot.implementation.moderation

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.MessageContext
import com.freezlex.jamesbot.internals.api.SlashContext
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

    fun run(ctx: Context, @Argument(type = OptionType.USER) member: Member) {
        if(member == ctx.messageContext?.author?: ctx.slashContext!!.author) return ctx.reply(ctx.language.category.moderation.ban.cannotSelfBan)
        if(ctx.isSlash()) slash(ctx.slashContext!!, member)
        else message(ctx.messageContext!!, member)
    }

    private fun slash(ctx: SlashContext, member: Member){

    }

    private fun message(ctx: MessageContext, member: Member){

    }
}
