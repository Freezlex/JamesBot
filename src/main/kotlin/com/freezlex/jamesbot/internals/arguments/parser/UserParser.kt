package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.CommandContext
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import net.dv8tion.jda.api.entities.User
import java.util.*

/**
 * The custom parser for the Boolean type
 */
class UserParser : Parser<User> {

    // TODO: Check ctx.message.mentionedUsers
    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: CommandContext, param: String): Optional<User> {
        val snowflake = snowflakeParser.parse(ctx, param)

        val user = if (snowflake.isPresent) {
            ctx.jda.getUserById(snowflake.get().resolved)
        } else {
            if (param.length > 5 && param[param.length - 5].toString() == "#") {
                val tag = param.split("#")
                ctx.jda.userCache.find { it.name == tag[0] && it.discriminator == tag[1] }
            } else {
                ctx.jda.userCache.find { it.name == param }
            }
        }

        return Optional.ofNullable(user)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}
