package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

/**
 * The custom parser for the Integer type
 */
class IntParser : Parser<Int> {

    /**
     * Parse the argument
     * @param ctx
     *          The CommandContext context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<Int> {
        return Optional.ofNullable(param.toIntOrNull())
    }
}
