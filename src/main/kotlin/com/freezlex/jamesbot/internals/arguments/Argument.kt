package com.freezlex.jamesbot.internals.arguments

import com.freezlex.jamesbot.internals.models.MessageModel
import java.lang.Exception

/**
 * Constructor for an argument
 * TODO : Same principe as the command registry
 */
interface Argument{
    val type: String
    val name: String
    val oneOf: List<Any>?
    val default: Any?

    fun execute(message: MessageModel): MessageModel{
        if(message.validateQueue.size == 0) throw Exception("Missing argument ${this.name}")
        this.validate(message)
        return message;
    }

    /**
     * Validate the argument before running the command to avoid mistake
     */
    fun validate(message: MessageModel): MessageModel{
        throw Error("Argument must have a run method")
    }

    /**
     * Running the argument parser to return
     */
    fun run(){
        throw Error("Argument must have a run method")
    }
}
