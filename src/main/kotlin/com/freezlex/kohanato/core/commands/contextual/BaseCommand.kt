package com.freezlex.kohanato.core.commands.contextual

import com.freezlex.kohanato.core.cooldown.Cooldown
import net.dv8tion.jda.api.Permission

interface BaseCommand {

    val name: String?
        get() = null
    val category: String?
        get() = null
    val cooldown: List<Cooldown>
        get() = listOf()
    val subSlashCommand: Boolean
        get() = true
    val aliases: MutableList<String>?
        get() = null
    val guildOnly: Boolean
        get() = true
    val botPermissions: List<Permission>
        get() = emptyList()

    fun onCommandError(command: Command, error: Throwable): Boolean = false

    fun localCheck(command: Command): Boolean = true
}