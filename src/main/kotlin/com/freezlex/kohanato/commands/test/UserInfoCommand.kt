package com.freezlex.kohanato.commands.test

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.Categories
import com.freezlex.kohanato.core.commands.contextual.UserContextCommand
import dev.minn.jda.ktx.Embed
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import java.time.format.DateTimeFormatter

class UserInfoCommand: UserContextCommand {

    override val name: String
        get() = "info"
    override val category: Categories
        get() = Categories.UNCATEGORIZED

    fun run(kl: KoListener) {
        val event = kl.event as UserContextInteractionEvent
        val member: Member = kl.event.targetMember?: return event.reply("This command must be used in a guild only.").queue()
        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        event.reply("Here's some data for you !").addEmbeds(Embed {
            title = "Informations about ${member.effectiveName}"
            description = if(member.nickname == null) "**${member.effectiveName}** doesn't use any nickname on this guild." else "**${member.user.name}** use the nickname **${member.nickname}** on this guild."
            thumbnail = member.effectiveAvatarUrl
            field("#️⃣ **| Discriminator :**", member.user.discriminator, true)
            field("\uD83C\uDD94 **| ID :**", member.user.id, true)
            field("\uD83C\uDFAB **| Roles :**", member.roles.joinToString(" ") { it.asMention }, false)
            field("\uD83D\uDEAA **| Joined this guild :**", member.timeJoined.format(format), true)
            field("\uD83D\uDCDD **| Account created :**", member.timeCreated.format(format), true)
        }).queue()
    }
}