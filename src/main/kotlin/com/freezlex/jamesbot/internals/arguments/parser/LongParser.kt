package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

class LongParser : Parser<Long> {
    override fun parse(ctx: Context, param: String): Optional<Long> {
        return Optional.ofNullable(param.toLongOrNull())
    }
}
