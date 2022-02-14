package com.freezlex.kohanato.core.commands

enum class Categories (val fName: String, val short: String?, val grouped: Boolean, val description: String) {
    MODERATION("moderation", "mod", true, "Moderation commands available as slash commands on the bot"),
    UNCATEGORIZED("uncategorized", null,false, "All the uncategorized commands as slash commands on the bot"),
}
