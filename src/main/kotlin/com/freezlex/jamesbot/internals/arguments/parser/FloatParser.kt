package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

class FloatParser : Parser<Float> {

    override fun parse(ctx: Context, param: String): Optional<Float> {
        return Optional.ofNullable(param.toFloatOrNull())
    }

}
