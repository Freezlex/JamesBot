package com.freezlex.jamesbot.implementation.arguments

import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.entities.Message
import kotlin.String
import kotlin.reflect.typeOf

/**
 * Implementation for STRING argument
 */
class StringType(
    override val type: String = "String",
    override val name: String,
    override val oneOf: List<Any>?,
    override val default: Any?,
    val length: Int,
): Argument{

    constructor(name:String, length: Int, oneOf: List<String>?, default: String?): this("String", name, oneOf, default, length)

    override fun validate(message: MessageModel): MessageModel {
        message.validateQueue.removeAt(0)
        return message;
    }

}
