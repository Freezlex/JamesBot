package com.freezlex.kohanato.core.commands.contextual

import dev.minn.jda.ktx.awaitButton
import dev.minn.jda.ktx.interactions.danger
import dev.minn.jda.ktx.interactions.primary
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

interface TextualCommand: BaseCommand {
    @OptIn(ExperimentalTime::class)
    suspend fun run(event: MessageReceivedEvent){
        val confirm = primary("${event.author.id}:did-forgot", "Yes", Emoji.fromUnicode("✅"))
        val deny = danger("${event.author.id}:did-not-forgot", "No", Emoji.fromUnicode("❌"))

        val msg = event.channel.sendMessage("Did the owner forgot to implement this command ?")
            .setActionRow(confirm, deny).complete()

        withTimeoutOrNull(20.seconds) { // 1 minute scoped timeout
            event.author.awaitButton(confirm).reply("je le savais ... J'en étais sûr !") // await for user to click button
        } ?: msg.editMessage("Timed out ... :)").setActionRow(confirm.asDisabled(), deny.asDisabled()).queue()
    }
}