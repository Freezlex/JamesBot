package com.freezlex.jamesbot.implementation.settings

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.commands.Cmd
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color

class LanguageCommand: Cmd {

    override fun name() = "language"
    override fun description(): String = "Edit the language of the bot for the guild or for the user"

    fun run(ctx: Context, @Argument(options = ["en_EN", "fr_FR"]) language: String?){
        if(language != null) setLanguageSettings(ctx, language)
        else showLanguageSettings(ctx)
    }

    private fun showLanguageSettings(ctx: Context){
        val userSettings = ClientCache.getUserSettings(ctx.messageContext?.author?: ctx.slashContext!!.author)
        var userEmbed: EmbedBuilder? = null
        if(userSettings != null) userEmbed = EmbedBuilder()
            .setAuthor(ctx.messageContext?.author?.name?: ctx.slashContext!!.author.name)
            .setTitle()

        val guildEmbed = EmbedBuilder()
            .setColor(Color.WHITE)
        if(ctx.isFromGuild())
    }

    private fun setLanguageSettings(ctx: Context, language: String){

    }
}
