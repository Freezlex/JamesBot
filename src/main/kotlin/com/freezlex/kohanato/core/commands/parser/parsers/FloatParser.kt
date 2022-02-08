package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.*

/**
 * The custom parser for the Float type
 */
class FloatParser : Parsed<Float> {

    /**
     * Parse the argument
     * @param ctx
     *          The CommandContext context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(event: SlashCommandInteractionEvent, param: String): Optional<Float> {
        return Optional.ofNullable(param.toFloatOrNull())
    }

}