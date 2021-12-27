package com.freezlex.kohanato

import com.freezlex.kohanato.api.Listener
import com.freezlex.kohanato.commands.test.TestSlashCommand
import com.freezlex.kohanato.commands.test.TestTextualCommand
import dev.minn.jda.ktx.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.ExperimentalTime

/**
 * Main application launcher
 */
@OptIn(ExperimentalTime::class)
fun main() {
    val jda = light(System.getenv("BOT_TOKEN"), enableCoroutines=true);
}

val logger: Logger = LoggerFactory.getLogger("Koahanto")
