package com.freezlex.jamesbot.implementation.arguments

import com.freezlex.jamesbot.internals.arguments.Argument
import net.dv8tion.jda.api.entities.Message
import kotlin.String

/**
 * Implementation for INTEGER argument
 */
class IntegerType (
    override var type: String = "Integer",
    override var length: Int,
    override var oneOf: List<Any>?,
    override var default: Any?
): Argument{

    constructor(length: Int, oneOf: List<Int>?, default: String?): this("String", length, oneOf, default)

    override fun validate(argument: String, type: Argument): Boolean {
        val number = argument.toIntOrNull() ?: return false
        if(oneOf?.contains(number) == false)return false;
        return true;
    }

}