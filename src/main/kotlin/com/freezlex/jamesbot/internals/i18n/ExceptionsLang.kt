package com.freezlex.jamesbot.internals.i18n

data class ExceptionsLang (
    val onCommandError: String,
    val onCommandPostInvoke: String,
    val onCommandPreInvoke: String,
    val onParseError: String,
    val onInternalError: String,
    val onCommandCooldown: String,
    val onBotMissingPermission: String,
    val onUserMissingPermission: String,
    val onUserMissingEarlyAccess: String,
    val onBadArgument: String,
    val onUnknownMessageCommand: String,
    val onUnknownSlashCommand: String)
