package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.util.*

class BooleanParser : Parser<Boolean> {

    override fun parse(ctx: Context, param: String): Optional<Boolean> {
        if (trueExpr.contains(param)) {
            return Optional.of(true)
        } else if (falseExpr.contains(param)) {
            return Optional.of(false)
        }

        return Optional.empty()
    }

    companion object {
        val trueExpr = listOf("yes", "y", "true", "t", "1", "enable", "on")
        val falseExpr = listOf("no", "n", "false", "f", "0", "disable", "off")
    }

}
