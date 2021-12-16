package com.freezlex.jamesbot.api.contextual

import dev.minn.jda.ktx.messages.send
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

interface SlashCommand: BaseCommand {
    fun run(event: SlashCommandEvent){
        event.channel.send("Default command effect");
    }
}