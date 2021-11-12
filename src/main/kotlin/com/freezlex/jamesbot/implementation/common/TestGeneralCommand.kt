package com.freezlex.jamesbot.implementation.common

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.MessageContext
import com.freezlex.jamesbot.internals.api.SlashContext
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import java.util.concurrent.TimeUnit

class TestGeneralCommand: Cmd {
    override fun name(): String = "Test"
    override fun category() = CommandCategory.UTILITY
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun description() = "Une commande pour avoir le ping du bot"

    fun run(ctx: Context){
        if(ctx.isSlash())this.slash(ctx.slashContext!!)
        else this.message(ctx.messageContext!!)
    }

    private fun slash(ctx: SlashContext){
        ctx.event.reply("🤖 The bot have a ping of `${ctx.jda.restPing.complete()} ms`").setEphemeral(true).mentionRepliedUser(true).queue()
    }

    private fun message(ctx: MessageContext){
        ctx.event.message.reply("🤖 The bot have a ping of `${ctx.jda.restPing.complete()} ms`").mentionRepliedUser(true).queue()
    }
}
