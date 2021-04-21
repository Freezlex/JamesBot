package com.freezlex.jamesbot.internals.event.listener

import com.freezlex.jamesbot.internals.event.DefaultListener
import net.dv8tion.jda.api.events.ReadyEvent
import org.springframework.stereotype.Component

/**
 * When the bot is ready
 */
@Component
class OnReadyListener: DefaultListener() {

    /**
     * When the bot is ready returned event from JDA
     */
    override fun onReady(event: ReadyEvent) {
        println("""
        ||-========================================================
        || Started JamesBot 0.0.1-SNAPSHOT
        || Account Info: ${event.jda.selfUser.name}#${event.jda.selfUser.discriminator} (ID: later bitchies)
        || Connected to ${event.jda.guilds.size} guilds, ${event.jda.textChannels.size} text channels
        ||-========================================================
        """.trimMargin("|"))
    }
}
