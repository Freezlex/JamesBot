package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.utils.CommandEvent
import net.dv8tion.jda.api.Permission

/**
 * Constructor for a command
 * TODO : Same principe as the command registry
 */
abstract class Command(
    val name : String,
    val alias: List<String>?,
    val arguments: List<Argument>?,
    val description : String?,
    val ownerOnly : Boolean,
    val userPermission : List<Permission>?,
    val botPermission : List<Permission>?
){
    /**
     * Runner for the command
     * @return Any
     */
    open fun run() : Boolean{
        return true;
    }
}
