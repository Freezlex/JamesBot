package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

/**
 * The custom parser for the Double type
 */
class DoubleParser : Parser<Double> {

    /**
     * Parse the argument
     * @param ctx
     *          The Context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<Double> {
        return Optional.ofNullable(param.toDoubleOrNull())
    }
}
