package com.freezlex.jamesbot.internals.i18n.category

data class CategoryLang(
    val description: String,
    val moderation: ModerationLang,
    val settings: SettingsLang,
    val common: CommonLang
)
