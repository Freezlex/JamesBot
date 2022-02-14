package com.freezlex.kohanato.core.commands.parser

import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import java.util.*

interface Parsed<T> {
    fun parse(event: GenericCommandInteractionEvent, param: String): Optional<T>
}