package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import net.dv8tion.jda.api.entities.VoiceChannel
import java.util.*

/**
 * The custom parser for the Boolean type
 */
class VoiceChannelParser : Parser<VoiceChannel> {

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<VoiceChannel> {
        val snowflake = snowflakeParser.parse(ctx, param)
        val channel: VoiceChannel? = if (snowflake.isPresent) {
            if(ctx.isSlash()) ctx.slashContext!!.guild?.getVoiceChannelById(snowflake.get().resolved)
            else ctx.messageContext!!.guild?.getVoiceChannelById(snowflake.get().resolved)
        } else {
            if(ctx.isSlash()) ctx.slashContext!!.guild?.voiceChannels?.firstOrNull { it.name == param }
            else ctx.messageContext!!.guild?.voiceChannels?.firstOrNull { it.name == param }
        }

        return Optional.ofNullable(channel)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}
