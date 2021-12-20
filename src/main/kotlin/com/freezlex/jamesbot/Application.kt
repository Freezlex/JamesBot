package com.freezlex.jamesbot

import com.freezlex.jamesbot.commands.test.TestTextualCommand
import dev.minn.jda.ktx.*
import dev.minn.jda.ktx.messages.Embeds
import dev.minn.jda.ktx.messages.send
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.components.Button
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import kotlin.time.ExperimentalTime

/**
 * Main application launcher
 */
@OptIn(ExperimentalTime::class)
fun main() {
    val jda = light(System.getenv("BOT_TOKEN"), enableCoroutines=true)

    jda.listener<MessageReceivedEvent> {
        val guild = it.guild
        val channel = it.channel
        val message = it.message
        val content = message.contentRaw

        if (content.startsWith("!test")) {
            // Send typing indicator and wait for it to arrive
            channel.sendTyping().await()
            TestTextualCommand().run(it)
        }
    }



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
