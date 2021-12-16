package com.freezlex.jamesbot.api.contextual

import dev.minn.jda.ktx.messages.send
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface TextualCommand: BaseCommand {

    fun run(event: MessageReceivedEvent){
        event.channel.send("Default command effect");
    }
}