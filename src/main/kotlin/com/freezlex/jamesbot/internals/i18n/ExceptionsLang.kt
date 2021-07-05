package com.freezlex.jamesbot.internals.i18n

data class ExceptionsLang (
    val onCommandCooldown: String,
    val onBotMissingPermission: String,
    val onUserMissingPermission: String,
    val onBadArgument: String,
    val onUnknownMessageCommand: String,
    val onUnknownSlashCommand: String,
    val onUserMissingEarlyAccess: String)
