package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import net.dv8tion.jda.api.entities.Role
import java.util.*

/**
 * The custom parser for the Role type
 */
class RoleParser : Parser<Role> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<Role> {
        val snowflake = snowflakeParser.parse(ctx, param)
        val role: Role? = if (snowflake.isPresent) {
            if(ctx.isSlash()) ctx.slashContext!!.guild?.getRoleById(snowflake.get().resolved)
            else ctx.messageContext!!.guild?.getRoleById(snowflake.get().resolved)
        } else {
            if(ctx.isSlash()) ctx.slashContext!!.guild?.roleCache?.firstOrNull { it.name == param }
            else ctx.messageContext!!.guild?.roleCache?.firstOrNull { it.name == param }
        }

        return Optional.ofNullable(role)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}
