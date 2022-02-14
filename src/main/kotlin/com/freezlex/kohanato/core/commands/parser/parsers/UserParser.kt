package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import java.util.*

/**
 * The custom parser for the Boolean type
 */
class UserParser : Parsed<User> {

    // TODO: Check ctx.message.mentionedUsers
    /**
     * Parse the argument
     * @param event
     *          The event
     * @param param
     *          The params to parse
     */
    override fun parse(event: GenericCommandInteractionEvent, param: String): Optional<User> {
        val snowflake = snowflakeParser.parse(event, param)

        val user = if (snowflake.isPresent) {
            event.jda.getUserById(snowflake.get().resolved)
        } else {
            if (param.length > 5 && param[param.length - 5].toString() == "#") {
                val tag = param.split("#")
                event.jda.userCache.find { it.name == tag[0] && it.discriminator == tag[1] }
            } else {
                event.jda.userCache.find { it.name == param }
            }
        }

        return Optional.ofNullable(user)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}