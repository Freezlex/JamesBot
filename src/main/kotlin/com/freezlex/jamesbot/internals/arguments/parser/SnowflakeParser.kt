package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import com.freezlex.jamesbot.internals.arguments.Snowflake
import java.util.*
import java.util.regex.Pattern

/**
 * The custom parser for the Snowflake type
 */
class SnowflakeParser : Parser<Snowflake> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<Snowflake> {
        val match = snowflakeMatch.matcher(param)

        if (match.matches()) {
            val id = match.group("sid") ?: match.group("id")
            return Optional.of(Snowflake(id.toLong()))
        }

        return Optional.empty()
    }

    companion object {
        private val snowflakeMatch = Pattern.compile("^(?:<(?:@!?|@&|#)(?<sid>[0-9]{17,21})>|(?<id>[0-9]{17,21}))$")
    }

}
