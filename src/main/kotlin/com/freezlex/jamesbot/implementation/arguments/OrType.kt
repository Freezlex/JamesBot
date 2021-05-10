package com.freezlex.jamesbot.implementation.arguments

import com.freezlex.jamesbot.internals.arguments.Argument
import net.dv8tion.jda.api.entities.Message

class OrType (
    override var type: String,
    override var length: Int,
    override var oneOf: List<Any>?,
    override var default: Any?

): Argument {

    constructor(length: Int, oneOf: List<String>?, default: String?): this("String", length, oneOf, default)

    override fun validate(argument: String, type: Argument): Boolean {
        throw Error("Argument must have a run method")
    }

}