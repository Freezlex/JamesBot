package com.freezlex.kohanato.core.commands.parser

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.*

interface Parsed<T> {
    fun parse(event: SlashCommandInteractionEvent, param: String): Optional<T>
}