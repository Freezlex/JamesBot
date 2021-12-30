package com.freezlex.kohanato.api.events

import com.freezlex.kohanato.commands.moderation.BanSlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

object OnSlashCommandEvent {
    suspend fun run(event: SlashCommandEvent){
        if(event.name == "moderation" && event.subcommandName == "ban"){
            BanSlashCommand().run(event)
        }
    }
}