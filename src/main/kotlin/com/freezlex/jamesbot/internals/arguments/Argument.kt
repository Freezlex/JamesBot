package com.freezlex.jamesbot.internals.arguments

import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.entities.Message

/**
 * Constructor for an argument
 * TODO : Same principe as the command registry
 */
interface Argument{
    var type: String
    var length: Int
    var oneOf: List<Any>?
    var default: Any?

    /**
     * Validate the argument before running the command to avoid mistake
     */
    open fun validate(argument: String, type: Argument): Boolean{
        throw Error("Argument must have a run method")
    }

    /**
     * Running the argument parser to return
     */
    fun run(){
        throw Error("Argument must have a run method")
    }
}
