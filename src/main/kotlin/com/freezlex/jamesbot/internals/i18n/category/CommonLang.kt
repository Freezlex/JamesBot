package com.freezlex.jamesbot.internals.i18n.category

import net.dv8tion.jda.api.entities.MessageEmbed

data class CommonLang (
    val help: HelpLang
    )

data class HelpLang(
    val description: String,
    val embed: MessageEmbed
)
