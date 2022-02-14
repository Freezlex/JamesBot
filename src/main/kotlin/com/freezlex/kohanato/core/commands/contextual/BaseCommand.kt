package com.freezlex.kohanato.core.commands.contextual

import com.freezlex.kohanato.core.commands.Categories
import com.freezlex.kohanato.core.cooldown.Cooldown
import dev.minn.jda.ktx.awaitButton
import dev.minn.jda.ktx.interactions.danger
import dev.minn.jda.ktx.messages.into
import dev.minn.jda.ktx.messages.reply_
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import kotlin.time.Duration.Companion.minutes

interface BaseCommand {

    val name: String?
        get() = null
    val description: String
        get() = "No description provided"
    val category: Categories
        get() = Categories.UNCATEGORIZED
    val cooldown: List<Cooldown>
        get() = listOf()
    val isSubGroup: Boolean
        get() = true
    val aliases: MutableList<String>?
        get() = null
    val guildOnly: Boolean
        get() = true
    val botPermissions: List<Permission>
        get() = emptyList()

    fun onCommandError(koCommand: KoCommand, error: Throwable): Boolean = false

    fun localCheck(koCommand: KoCommand): Boolean = true

    suspend fun run(event: GenericCommandInteractionEvent){
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
