package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

class IntParser : Parser<Int> {

    override fun parse(ctx: Context, param: String): Optional<Int> {
        return Optional.ofNullable(param.toIntOrNull())
    }

}
