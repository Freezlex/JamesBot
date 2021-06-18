package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.CommandContext
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.SlashContext
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

/**
 * The custom parser for the Double type
 */
class DoubleParser : Parser<Double> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: CommandContext, param: String): Optional<Double> {
        return Optional.ofNullable(param.toDoubleOrNull())
    }

    /**
     * Parse the argument
     * @param ctx
     *          The SlashContext context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: SlashContext, param: String): Optional<Double> {
        return Optional.ofNullable(param.toDoubleOrNull())
    }
}
