package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class OnSlashCommandEvent {
    companion object{
        fun run(executor: ExecutorClient, event: SlashCommandEvent){
            logger.debug("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")
        }
    }
}
