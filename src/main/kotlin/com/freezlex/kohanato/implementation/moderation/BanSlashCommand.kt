package com.freezlex.kohanato.implementation.moderation

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.Categories
import com.freezlex.kohanato.core.commands.arguments.Param
import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import com.freezlex.kohanato.core.cooldown.BucketType
import com.freezlex.kohanato.core.cooldown.Cooldown
import dev.minn.jda.ktx.events.awaitButton
import dev.minn.jda.ktx.interactions.components.danger
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class BanSlashCommand: SlashCommand {

    override val name: String
        get() = "ban"
    override val category: Categories
        get() = Categories.MODERATION
    override val aliases: MutableList<String>
        get() = mutableListOf("b", "vatefaire")
    override val cooldown: List<Cooldown>
        get() = listOf(Cooldown(1.minutes, BucketType.USER))

    suspend fun run( core: KoListener, event: SlashCommandInteractionEvent,
        @Param(name = "member", description = "The member that should be banned")
        member: User,
        @Param(name = "duration", description = "How much time must this user be banned for.")
        duration: Duration? = null,
        @Param(name= "delete_messages", description = "How much of their recent message history to delete.")
        messageDel: Duration? = null,
        @Param(name = "reason", description = "The reason for banning, if any.")
        reason: String? = null)
    {
        val confirm = danger("${member.id}:ban", "Confirm")
        val reply = event.reply("Are you sure you want to ban **${member.asTag}**?").setEphemeral(true).addActionRow(confirm).complete()

        withTimeoutOrNull(1.minutes) { // 1 minute scoped timeout
            val pressed = event.user.awaitButton(confirm) // await for user to click button
            pressed.deferEdit().queue() // Acknowledge the button press
            try{
                event.guild?.ban(member, duration?.inWholeDays?.toInt() ?: 1, event.getOption("reason")?.asString?: "No reason provided")?.queue() // the button is pressed -> execute action
                reply.editOriginal("**${member.name}** has been banned for ${duration?.inWholeDays ?: "Undefined"} days.")
                    .setActionRow(confirm.asDisabled())
                    .queue()
            }catch (e: Exception){
                reply.editOriginal("Oh ... I was unable to ban **${member.name}**")
                    .setActionRow(confirm.asDisabled())
                    .queue()
            }
        } ?: reply.editOriginal("Action timed out").setActionRow(confirm.asDisabled()).queue()
    }
}
