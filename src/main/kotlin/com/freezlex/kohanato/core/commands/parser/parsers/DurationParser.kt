package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import com.freezlex.kohanato.core.extensions.asDuration
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import java.util.*
import kotlin.time.Duration

class DurationParser: Parsed<Duration> {
    override fun parse(event: GenericCommandInteractionEvent, param: String): Optional<Duration> {
        val time = param.asDuration();
        return Optional.ofNullable(time)
    }
}