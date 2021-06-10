package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

class StringParser : Parser<String> {

    override fun parse(ctx: Context, param: String): Optional<String> {
        if (param.isEmpty() || param.isBlank()) {
            return Optional.empty()
        }

        return Optional.of(param)
    }

}
