package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.CommandContext
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
    override fun parse(ctx: CommandContext, param: String): Optional<VoiceChannel> {
        val snowflake = snowflakeParser.parse(ctx, param)
        val channel: VoiceChannel? = if (snowflake.isPresent) {
            ctx.guild?.getVoiceChannelById(snowflake.get().resolved)
        } else {
            ctx.guild?.voiceChannels?.firstOrNull { it.name == param }
        }

        return Optional.ofNullable(channel)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}
