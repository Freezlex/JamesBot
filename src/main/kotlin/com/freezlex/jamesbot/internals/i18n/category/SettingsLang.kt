package com.freezlex.jamesbot.internals.i18n.category

import com.freezlex.jamesbot.internals.i18n.ArgumentLang
import net.dv8tion.jda.api.entities.MessageEmbed

data class SettingsLang(
    val language: LanguageLang
)

data class LanguageLang(
    val description: String,
    val language: String,
    val isIdentical: String,
    val error: String,
    val embed: MessageEmbed,
    val guildEmbed: MessageEmbed,
    val arguments: List<ArgumentLang>
)
