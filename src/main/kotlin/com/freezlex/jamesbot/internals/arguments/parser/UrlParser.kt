package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.net.URL
import java.util.*

class UrlParser : Parser<URL> {

    override fun parse(ctx: Context, param: String): Optional<URL> {
        return try {
            Optional.of(URL(param))
        } catch (e: Throwable) {
            Optional.empty()
        }
    }

}
