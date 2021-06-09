package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.internals.client.ExecutorClient
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class OnMessageReceivedEvent {
    companion object{
        fun run(executor: ExecutorClient, event: MessageReceivedEvent){
            if(event.author.isBot || event.isWebhookMessage)return

            if(event.isFromGuild)val parsed = executor.clientCache.getPrefixPattern(event.message).find(event.message.contentRaw) ?: return
            println(parsed.groupValues)
        }
    }
}
