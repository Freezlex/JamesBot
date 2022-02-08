package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.*

/**
 * The custom parser for the Boolean type
 */
class BooleanParser : Parsed<Boolean> {

    /**
     * Parse the argument
     * @param ctx
     *          The Context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(event: SlashCommandInteractionEvent, param: String): Optional<Boolean> {
        if (trueExpr.contains(param)) {
            return Optional.of(true)
        } else if (falseExpr.contains(param)) {
            return Optional.of(false)
        }

        return Optional.empty()
    }

    companion object {
        val trueExpr = listOf("yes", "y", "true", "t", "1", "enable", "on")
        val falseExpr = listOf("no", "n", "false", "f", "0", "disable", "off")
    }
}