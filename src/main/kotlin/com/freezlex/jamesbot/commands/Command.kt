package com.freezlex.jamesbot.commands

import com.freezlex.jamesbot.utils.ChatUtil
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.function.Consumer

abstract class Command(
    val name: String,
    val aliases: Array<String> = arrayOf(name),
    val ownerOnly: Boolean = true
    ): EventListener{

    init {
        register()
    }

    abstract fun execute(args: List<String>, e: MessageReceivedEvent)

    private fun register() = CommandsRegistry.registerCommand(this)

    fun String.toMessage(): Message = MessageBuilder().append(this).build()

    fun MessageReceivedEvent.reply(msg: Message, success: Consumer<Message>? = null)
            = ChatUtil(this).reply(msg, success)

    fun MessageReceivedEvent.reply(embed: MessageEmbed, success: Consumer<Message>? = null)
            = ChatUtil(this).reply(embed, success)

    fun MessageReceivedEvent.reply(text: String, success: Consumer<Message>? = null)
            = ChatUtil(this).reply(text, success)
}
