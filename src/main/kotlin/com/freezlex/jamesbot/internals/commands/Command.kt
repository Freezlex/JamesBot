package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.utils.CommandEvent
import java.security.Permission

/**
 * Constructor for a command
 * TODO : Same principe as the command registry
 */
interface Command {
    val name : String
    val alias: List<String>
        get() = listOf(name)
    val description : String
    val ownerOnly : Boolean
    val userPermission : List<Permission>
    val botPermission : List<Permission>

    fun run(args : List<Argument>, event: CommandEvent) : Any
}
