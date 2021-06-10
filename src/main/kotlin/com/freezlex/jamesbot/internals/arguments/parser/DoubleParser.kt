package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

class DoubleParser : Parser<Double> {

    override fun parse(ctx: Context, param: String): Optional<Double> {
        return Optional.ofNullable(param.toDoubleOrNull())
    }

}
