package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission

interface Cmd {

    fun name(): String? = null
    fun category(): String? = null
    fun aliases(): List<String>? = null
    fun description(): String? = "No description available"
    fun cooldown(): Cooldown? = Cooldown(duration = 5, bucket = BucketType.USER)
    fun developerOnly(): Boolean = false
    fun userPermissions(): List<Permission> = listOf()
    fun botPermissions(): List<Permission> = listOf()
    fun guildOnly(): Boolean = false
    fun enabled(): Boolean = true
    fun hidden(): Boolean = false

    fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable): Boolean = false

    fun localCheck(ctx: Context, command: CommandFunction): Boolean = true

}
