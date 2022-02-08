package com.freezlex.kohanato.core.commands.contextual

import dev.minn.jda.ktx.awaitButton
import dev.minn.jda.ktx.interactions.danger
import dev.minn.jda.ktx.messages.into
import dev.minn.jda.ktx.messages.reply_
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

interface SlashCommand: BaseCommand {

    @OptIn(ExperimentalTime::class)
    suspend fun run(event: SlashCommandInteractionEvent){
        val confirm = danger("${event.user.id}:default", "Yes, I guess")
        event.reply_(
            "Did the **Freezlex** forgot to implement this command ?",
            components=confirm.into(),
            ephemeral=true
        ).queue()

        withTimeoutOrNull(1.minutes) { // 1 minute scoped timeout
            val confirmed = event.user.awaitButton(confirm) // await for user to click button
            confirmed.deferEdit().queue()
            event.hook.editOriginal("I knew it ... **Freezlex** you're dumb !").setActionRow(confirm.asDisabled()).queue();
        } ?: event.hook.editOriginal("You haven't replied I guess it's a nope then...").setActionRow(confirm.asDisabled()).queue()
    }
}
