package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import java.util.*
import java.util.regex.Pattern

/**
 * The custom parser for the Snowflake type
 */
class SnowflakeParser : Parsed<Snowflake> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(event: GenericCommandInteractionEvent, param: String): Optional<Snowflake> {
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

class Snowflake(val resolved: Long)
// Exists solely for the snowflake parser.