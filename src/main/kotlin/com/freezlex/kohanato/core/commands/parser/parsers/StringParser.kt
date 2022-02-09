package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.*

/**
 * The custom parser for the String type
 */
class StringParser : Parsed<String> {

    /**
     * Si tu lis ça c'est que vraiment tu te fais chier à comprendre tout le code ... ou alors que y'a un bug ...
     * Mais un bug pour 6 lignes c'est chaud quand même !
     */

    /**
     * Parse the argument
     * @param ctx
     *          The context of the event
     * @param param
     *          The params to parse
     */
    override fun parse(event: SlashCommandInteractionEvent, param: String): Optional<String> {
        if (param.isEmpty() || param.isBlank()) {
            return Optional.empty()
        }

        return Optional.of(param)
    }

}