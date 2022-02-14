package com.freezlex.kohanato.commands.test

import com.freezlex.kohanato.core.commands.Categories
import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CompleteCapabilitiesSlashCommand: SlashCommand {

    override val name: String
        get() = "Complete"
    override val description: String
        get() = "This is a complete example of Kohanato capabilities"
    override val category: Categories
        get() = Categories.UNCATEGORIZED

    fun run(event: SlashCommandInteractionEvent, member: Member) {
        event.reply("Selected user : ${member.nickname}")
    }
}