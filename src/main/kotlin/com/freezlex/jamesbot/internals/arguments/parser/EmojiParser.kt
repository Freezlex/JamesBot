package com.freezlex.jamesbot.internals.arguments.parser

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Parser
import com.freezlex.jamesbot.internals.entities.Emoji
import java.util.*
import java.util.regex.Pattern

/**
 * The custom parser for the Emoji type
 */
class EmojiParser : Parser<Emoji> {

    // TODO: Support unicode emoji?
    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(ctx: Context, param: String): Optional<Emoji> {
        return parseEmoji(param)
    }

    private fun parseEmoji(param: String): Optional<Emoji>{
        val match = EMOJI_REGEX.matcher(param)

        if (match.find()) {
            val isAnimated = match.group(1) != null
            val name = match.group(2)
            val id = match.group(3).toLong()

            return Optional.of(Emoji(name, id, isAnimated))
        }

        return Optional.empty()
    }

    companion object {
        val EMOJI_REGEX = Pattern.compile("<(a)?:(\\w+):(\\d{17,21})")!!
    }

}
