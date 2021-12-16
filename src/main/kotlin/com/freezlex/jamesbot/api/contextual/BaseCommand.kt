package com.freezlex.jamesbot.api.contextual

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface BaseCommand {

    val category: String;

    public fun onTextualCommand(event: MessageReceivedEvent){

    }

    public fun onSlashCommand(event: SlashCommandEvent){

    }

    public fun run(){

    }
}