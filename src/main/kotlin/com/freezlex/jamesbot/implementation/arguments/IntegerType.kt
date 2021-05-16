package com.freezlex.jamesbot.implementation.arguments

import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.models.ArgumentModel
import com.freezlex.jamesbot.internals.models.MessageModel
import java.lang.Exception
import kotlin.String

/**
 * Implementation for INTEGER argument
 */
class IntegerType (
    override val type: String,
    override val name: String,
    override val oneOf: List<Any>?,
    override val default: Any?,
    private val maximum: Int?,
    private val minimum: Int?
): Argument{

    constructor(name:String, maximum: Int?, minimum: Int?, oneOf: List<Int>?, default: Int?): this("Integer", name, oneOf, default, maximum, minimum)

    /**
     * Create the argument by providing a name
     * @param name Name of the argument
     */
    constructor(name: String): this(name, null, null, null, null)

    /**
     * Create the argument by providing a name, a oneOf list and a default value
     * @param name Name of the argument
     * @param oneOf List of possible value for the argument
     * @param default Default value for the argument
     */
    constructor(name: String, oneOf: List<Int>, default: Int?): this(name, null, null, oneOf, default)

    /**
     * Create the argument by providing a name, a maximum value, a minimum value and a default value
     * @param name Name of the argument
     * @param maximum Maximum available value
     * @param minimum Minimum available value
     * @param default Default value for the argument
     */
    constructor(name: String, maximum: Int?, minimum: Int?, default: Int?): this(name, maximum, minimum, null, default)

    override fun validate(message: MessageModel): ArgumentModel {
        val number = message.validateQueue[0].toIntOrNull() ?: throw Exception("The argument ${this.name} must be a number")
        if(oneOf != null){
            if(!oneOf.contains(number))throw Exception("The argument ${this.name} must be one of `${this.oneOf.joinToString("`, `")}`")
        }else{
            if(this.maximum != null && this.maximum < number)throw Exception("$number is too high. The argument must be lower than ${this.maximum}")
            if(this.minimum != null && this.minimum > number)throw Exception("$number is too low. The argument must be higher than ${this.minimum}")
        }
        return ArgumentModel(number, this);
    }
}
