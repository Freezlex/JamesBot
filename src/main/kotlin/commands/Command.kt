package com.freezlex.jamesbot.commands

import com.freezlex.jamesbot.utils.Arguments
import com.freezlex.jamesbot.utils.CommandEvent
import java.security.Permission

interface Command {
    val name : String
    val alias: List<String>
        get() = listOf(name)
    val description : String
    val ownerOnly : Boolean
    val userPermission : List<Permission>
    val botPermission : List<Permission>

    fun run(args : List<Arguments>, event: CommandEvent) : Any
}
