package com.freezlex.jamesbot

import dev.minn.jda.ktx.*
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.components.Button
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Main application launcher
 */
fun main() {
    val jda = light("NDI3NDE2MDY1MjE2MDg2MDE2.Wrd8Og.55BN_kdd6R9IvxEwRyXsv3ygbuc", enableCoroutines=true)

    jda.listener<MessageReceivedEvent> {
        val guild = it.guild
        val channel = it.channel
        val message = it.message
        val content = message.contentRaw

        if (content.startsWith("!profile")) {
            // Send typing indicator and wait for it to arrive
            channel.sendTyping().await()
            val user = message.mentionedUsers.firstOrNull() ?: run {
                // Try loading user through prefix loading
                val matches = guild.retrieveMembersByPrefix(content.substringAfter("!profile "), 1).await()
                // Take first result, or null
                matches.firstOrNull()
            }

            if (user == null) // unknown user for name
                channel.sendMessageFormat("%s, I cannot find a user for your query!", it.author).queue()
            else // load profile and send it as embed
                channel.sendMessageFormat("%s, here is the user profile:", it.author)
                    .queue()
        }
    }

    val test = HashMap<String, suspend CoroutineEventListener.(SlashCommandEvent) -> Unit>()

    val event =
    test.put("ban",);

    jda.onCommand("moderation") { event ->

        if(event.subcommandName == "ban"){
            val user = event.getOption("member")!!.asUser
            val confirm = Button.danger("${user.id}:ban", "Confirm")
            event.reply("Are you sure you want to ban **${user.asTag}**?")
                .addActionRow(confirm)
                .setEphemeral(true)
                .queue()

            withTimeoutOrNull(60000) { // 1 minute timeout
                val pressed = event.user.awaitButton(confirm) // await for user to click button
                pressed.deferEdit().queue() // Acknowledge the button press
                event.guild?.ban(user, 0)?.queue() // the button is pressed -> execute action
            } ?: event.hook.editOriginal("Action canceled").setActionRows(emptyList()).queue()
        }
    }

    jda.onButton("hello") { // Button that says hello
        it.reply("Hello :)").queue()
    }
}

val logger: Logger = LoggerFactory.getLogger("JamesBot")
