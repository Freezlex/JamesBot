package com.freezlex.jamesbot.implementation.settings

import com.freezlex.jamesbot.database.entities.UserSettings
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.arguments.Test
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.i18n.LanguageModel
import com.freezlex.jamesbot.internals.i18n.Languages
import com.freezlex.jamesbot.logger
import io.ktor.util.*
import net.dv8tion.jda.api.EmbedBuilder
import org.jetbrains.exposed.sql.transactions.transaction

class LanguageCommand: Cmd {

    override fun name() = "language"
    override fun aliases() = mutableListOf("lang")
    override fun description(): String = "Edit the language of the bot for the guild or for the user"

    fun run(ctx: Context, @Argument(options = ["français", "english"]) language: String?){
        if(language != null) setLanguageSettings(ctx, language)
        else showLanguageSettings(ctx)
    }

    /**
     * If no params we're displaying the current settings
     * @param ctx The context of the message
     */
    private fun showLanguageSettings(ctx: Context){
        val userSettings = ClientCache.getUserSettings(ctx.messageContext?.author?: ctx.slashContext!!.author)
        val settingsEmbed = EmbedBuilder(ctx.language.category.settings.language.embed)
        val userField = settingsEmbed.fields[0]
        val guildField = settingsEmbed.fields[1]
        settingsEmbed.clearFields()

        if(userSettings != null){
            val user = ctx.messageContext?.author?: ctx.slashContext!!.author

            settingsEmbed.addField(
                userField.name!!,
                userField.value!!.format(
                    ctx.language.common.langCde[userSettings.regCde]!!.lowercase(),
                    user.name),
                userField.isInline)
        }

        if(ctx.isFromGuild()){
            val guild = ctx.messageContext?.guild?: ctx.slashContext!!.guild
            settingsEmbed.addField(
                guildField.name,
                guildField.value!!.format(
                    ctx.language.common.langCde[ClientCache.getGuildSettingsOrCreate(guild!!).regCde]!!.lowercase(),
                    ctx.messageContext?.guild?.name?: ctx.slashContext!!.guild?.name),
                guildField.isInline)
        }

        ctx.reply("🌍", settingsEmbed.build())
    }

    /**
     * If there is a language code set we're settings it up for the guild or for the user
     * @param ctx The context of the message
     * @param language The desired language
     */
    private fun setLanguageSettings(ctx: Context, language: String){
        val lang = resolveLanguage(language)

        val author = ctx.messageContext?.author?: ctx.slashContext!!.author
        val userSettings = ClientCache.getUserSettings(author)

        ctx.language = resolveLanguage(language)

        if(ctx.isFromGuild()){
            val guild = ctx.messageContext?.guild?: ctx.slashContext!!.guild!!
            if(guild.owner == author){
                val setting = ClientCache.getGuildSettingsOrCreate(guild)
                if(language == setting.regCde) return ctx.reply(ctx.language.category.settings.language.isIdentical.format(setting.regCde), true)
                try{
                    transaction { setting.regCde = lang.code }
                    ctx.reply(ctx.language.category.settings.language.hasBeenSet.format(guild.name, setting.regCde))
                }catch(e: Exception){
                    logger.error(e)
                    return ctx.reply(ctx.language.category.settings.language.error.format(setting.regCde), true)
                }
            }else{
                setUserLang(userSettings, ctx)
            }
        }else{
            setUserLang(userSettings, ctx)
        }
    }

    private fun setUserLang(setting: UserSettings?, ctx: Context){
        var st = setting
        if(st == null)st = ClientCache.getUserSettingsOrCreate(ctx.messageContext?.author?: ctx.slashContext!!.author)
        if(ctx.language.code == st.regCde) return ctx.reply(ctx.language.category.settings.language.isIdentical.format(st.regCde), true)
        try{
            transaction { st.regCde = ctx.language.code }
            ctx.reply(ctx.language.category.settings.language.hasBeenSet.format(ctx.messageContext?.author?.name?: ctx.slashContext!!.author.name, st.regCde))
        }catch (e: Exception){
            logger.error(e)
            return ctx.reply(ctx.language.category.settings.language.error.format(ctx.language.code))
        }
    }

    private fun resolveLanguage(language: String): LanguageModel {
        val res = Languages.filter { it.value.name.lowercase() == language.lowercase() }
        return res.values.first()
    }
}
