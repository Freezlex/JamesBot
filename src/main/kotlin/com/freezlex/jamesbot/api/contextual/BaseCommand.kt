package com.freezlex.jamesbot.api.contextual

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface BaseCommand {

    val category: String
        get() = "Test"
}