package com.freezlex.jamesbot.implementation.settings

import com.freezlex.jamesbot.database.entities.UserSettings
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.arguments.Test
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.i18n.LanguageModel
import com.freezlex.jamesbot.internals.i18n.Languages
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.transactions.transaction

class LanguageCommand: Cmd {

    override fun name() = "language"
    override fun aliases() = mutableListOf("lang")
    override fun description(): String = "Edit the language of the bot for the guild or for the user"

    fun run(ctx: Context, @Argument(options = ["Français", "English"]) language: String?){
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
                    user.name,
                    ctx.language.common.langCde[userSettings.regCde]!!.lowercase()),
                userField.isInline)
        }

        if(ctx.isFromGuild()){
            val guild = ctx.messageContext?.guild?: ctx.slashContext!!.guild
            settingsEmbed.addField(
                guildField.name,
                guildField.value!!.format(
                    ctx.messageContext?.guild?.name?: ctx.slashContext!!.guild?.name,
                    ctx.language.common.langCde[ClientCache.getGuildSettingsOrCreate(guild!!).regCde]!!.lowercase()),
                guildField.isInline)
        }

        ctx.reply("", settingsEmbed.build())
    }

    /**
     * If there is a language code set we're settings it up for the guild or for the user
     * @param ctx The context of the message
     * @param language The desired language
     */
    private fun setLanguageSettings(ctx: Context, language: String){
        val lang = resolveLanguage(language)

        val author = ctx.messageContext?.author?: ctx.slashContext!!.author
        var userSettings = ClientCache.getUserSettings(author)

        ctx.language = resolveLanguage(language)

        if(ctx.isFromGuild()){
            val guild = ctx.messageContext?.guild?: ctx.slashContext!!.guild!!
            if(guild.owner == author){
                val setting = ClientCache.getGuildSettingsOrCreate(guild)
                try{
                    transaction { setting.regCde = lang.code }
                    if(language == setting.regCde) return ctx.reply(ctx.language.category.settings.language.isIdentical.format(setting.regCde), true)
                }catch(e: Exception){
                    return ctx.reply(ctx.language.category.settings.language.error.format(setting.regCde, , e.message), true)
                }
            }else{
                if(userSettings == null)userSettings = ClientCache.getUserSettingsOrCreate(author)
                try{
                    transaction { userSettings.regCde = lang.code }
                    if(language == userSettings.regCde){
                        return ctx.reply(ctx.language.category.settings.language.isIdentical.format(userSettings.regCde), true)
                    }
                }catch (e: Exception){
                    return ctx.reply(ctx.language.category.settings.language.error.format(userSettings.regCde, author, e.message), true)
                }
            }
        }else{

        }
    }

    private fun setUserLang(st: UserSettings?, lg: String, ctx: Context){
        var set = st
        if(set == null)set = ClientCache.getUserSettingsOrCreate(ctx.)
        try{
            transaction { set.regCde = lg }
        }catch (e: Exception){
            ctx.reply(ctx.language.category.settings.language.error.format())
        }
    }

    private fun resolveLanguage(language: String): LanguageModel = Languages.filter { it.value.name == language }.values.first()
}
