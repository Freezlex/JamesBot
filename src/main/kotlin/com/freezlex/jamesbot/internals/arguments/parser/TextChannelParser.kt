package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import net.dv8tion.jda.api.entities.TextChannel
import java.util.*

/**
 * The custom parser for the TextChannel type
 */
class TextChannelParser : Parser<TextChannel> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<TextChannel> {
        val snowflake = snowflakeParser.parse(ctx, param)
        val channel: TextChannel? = if (snowflake.isPresent) {
            if(ctx.isSlash()) ctx.slashContext!!.guild?.getTextChannelById(snowflake.get().resolved)
            else ctx.messageContext!!.guild?.getTextChannelById(snowflake.get().resolved)
        } else {
            if(ctx.isSlash()) ctx.slashContext!!.guild?.textChannels?.firstOrNull { it.name == param }
            else ctx.messageContext!!.guild?.textChannels?.firstOrNull { it.name == param }
        }

        return Optional.ofNullable(channel)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}
