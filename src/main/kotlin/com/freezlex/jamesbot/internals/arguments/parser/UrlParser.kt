package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import java.net.URL
import java.util.*

/**
 * The custom parser for the Url type
 */
class UrlParser : Parser<URL> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<URL> {
        return try {
            Optional.of(URL(param))
        } catch (e: Throwable) {
            Optional.empty()
        }
    }

}
