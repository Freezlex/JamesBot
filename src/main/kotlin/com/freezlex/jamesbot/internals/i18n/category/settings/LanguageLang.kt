package com.freezlex.jamesbot.internals.i18n.category.settings

import com.freezlex.jamesbot.internals.i18n.ArgumentLang

data class LanguageLang(
    val description: String,
    val language: String,
    val cannotBan: String,
    val cannotSelfBan: String,
    val hasBeenBanned: String,
    val arguments: List<ArgumentLang>
)
