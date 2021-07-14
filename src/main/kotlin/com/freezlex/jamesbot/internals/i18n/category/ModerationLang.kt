package com.freezlex.jamesbot.internals.i18n.category

import com.freezlex.jamesbot.internals.i18n.ArgumentLang

data class ModerationLang(
    val ban: BanLang
)

data class BanLang(
    val description: String,
    val member: String,
    val cannotBan: String,
    val cannotSelfBan: String,
    val hasBeenBanned: String,
    val arguments: List<ArgumentLang>)
