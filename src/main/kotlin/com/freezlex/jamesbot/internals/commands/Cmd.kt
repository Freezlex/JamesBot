package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.api.Context
import java.security.Permission

interface Cmd {

    fun name(): String? = null
    fun aliases(): List<String>? = null
    fun description(): String? = "No description available"
    fun developerOnly(): Boolean = false
    fun userPermissions(): MutableList<Permission>? = null;
    fun botPermissions(): MutableList<Permission>? = null;
    fun argsDelimiter():Char = ' '
    fun guildOnly(): Boolean = false
    fun enabled(): Boolean = true
    fun hidden(): Boolean = false

    fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable): Boolean = false

    fun localCheck(ctx: Context, command: CommandFunction): Boolean = true

}
