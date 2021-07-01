package com.freezlex.jamesbot.internals.i18n.category.moderation

import com.freezlex.jamesbot.internals.i18n.ArgumentLang

data class BanLang(
    val description: String,
    val member: String,
    val cannotBan: String,
    val hasBeenBanned: String,
    val arguments: List<ArgumentLang>)
