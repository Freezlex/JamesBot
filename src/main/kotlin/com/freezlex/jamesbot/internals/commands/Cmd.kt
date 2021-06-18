package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.api.CommandContext
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission

interface Cmd {

    fun name(): String? = null
    fun category(): CommandCategory = CommandCategory.UNCATEGORIZED
    fun aliases(): MutableList<String>? = null
    fun description(): String = "No description available"
    fun cooldown(): Cooldown? = Cooldown(duration = 5, bucket = BucketType.USER)
    fun isDeveloperOnly(): Boolean = false
    fun isPreview(): Boolean = false
    fun isSlash(): Boolean = true
    fun userPermissions(): List<Permission> = listOf()
    fun botPermissions(): List<Permission> = listOf()
    fun isGuildOnly(): Boolean = false
    fun isEnabled(): Boolean = true
    fun isHidden(): Boolean = false

    fun onCommandError(ctx: CommandContext, command: CommandFunction, error: Throwable): Boolean = false

    fun localCheck(ctx: Context, command: CommandFunction): Boolean = true

}
