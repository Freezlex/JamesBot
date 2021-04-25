package com.freezlex.jamesbot.internals.utils

import com.freezlex.jamesbot.internals.arguments.Argument

/**
 * Command Event
 */
class CommandEvent (var prefix: String, var args: MutableList<Argument>){

    fun addAllArgs(argument: MutableList<Argument>){
        args.addAll(argument)
    }

    fun addArgs(argument: Argument){
        args.add(argument)
    }
}
