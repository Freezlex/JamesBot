package com.freezlex.jamesbot.internals.commands

enum class CommandCategory(val category: String, val short: String?,val regrouped: Boolean, val description: String) {
    MODERATION("moderation", "mod",true, "Moderation commands available as slash commands on the bot"),
    UTILITY("utility", "util",true, "Utility commands available as slash commands on the bot"),
    SETTINGS("settings", "set", false, "Settings commands available as slash commands on the bot"),
    UNCATEGORIZED("uncategorized", null,false, "All the uncategorized commands as slash commands on the bot"),
}
