package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.CommandContext
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.SlashContext
import com.freezlex.jamesbot.internals.arguments.Parser
import net.dv8tion.jda.api.entities.Member
import java.util.*

/**
 * The custom parser for the Member type
 */
class MemberParser : Parser<Member> {

    // TODO: Check ctx.message.mentionedMembers
    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: CommandContext, param: String): Optional<Member> {
        val snowflake = snowflakeParser.parse(ctx, param)

        val member: Member? = if (snowflake.isPresent) {
            ctx.guild?.getMemberById(snowflake.get().resolved)
        } else {
            if (param.length > 5 && param[param.length - 5].toString() == "#") {
                val tag = param.split("#")
                ctx.guild?.memberCache?.find { it.user.name == tag[0] && it.user.discriminator == tag[1] }
            } else {
                ctx.guild?.getMembersByName(param, false)?.firstOrNull()
            }
        }

        return Optional.ofNullable(member)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}
