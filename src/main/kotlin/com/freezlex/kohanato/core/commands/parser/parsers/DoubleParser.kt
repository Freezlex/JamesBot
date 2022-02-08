package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.*

/**
 * The custom parser for the Double type
 */
class DoubleParser : Parsed<Double> {

    /**
     * Parse the argument
     * @param ctx
     *          The Context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(event: SlashCommandInteractionEvent, param: String): Optional<Double> {
        return Optional.ofNullable(param.toDoubleOrNull())
    }
}