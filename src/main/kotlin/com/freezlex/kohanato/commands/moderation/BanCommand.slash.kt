package com.freezlex.kohanato.commands.moderation

import com.freezlex.kohanato.api.contextual.SlashCommand
import com.freezlex.kohanato.api.extensions.asDuration
import dev.minn.jda.ktx.awaitButton
import dev.minn.jda.ktx.interactions.danger
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import java.lang.Exception
import kotlin.time.Duration.Companion.minutes

class BanSlashCommand: SlashCommand {

    override suspend fun run(event: SlashCommandEvent) {
        val user = event.getOption("member")!!.asUser
        val duration = event.getOption("duration")?.asString?.asDuration()
        val confirm = danger("${user.id}:ban", "Confirm")
        val reply = event.reply("Are you sure you want to ban **${user.asTag}**?").setEphemeral(true).addActionRow(confirm).complete()

        withTimeoutOrNull(1.minutes) { // 1 minute scoped timeout
            val pressed = event.user.awaitButton(confirm) // await for user to click button
            pressed.deferEdit().queue() // Acknowledge the button press
            try{
                event.guild?.ban(user, duration?.inWholeDays?.toInt() ?: 1, event.getOption("reason")?.asString?: "No reason provided")?.queue() // the button is pressed -> execute action
                reply.editOriginal("**${user.name}** has been banned for ${duration?.inWholeDays?: "Undefined"} days.")
                    .setActionRow(confirm.asDisabled())
                    .queue()
            }catch (e: Exception){
                reply.editOriginal("Oh ... I was unable to ban **${user.name}**")
                    .setActionRow(confirm.asDisabled())
                    .queue()
            }
        } ?: reply.editOriginal("Action timed out").setActionRow(confirm.asDisabled()).queue()
    }
}
