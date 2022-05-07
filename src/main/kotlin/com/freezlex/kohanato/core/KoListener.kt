package com.freezlex.kohanato.core

import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.commands.parser.Parser
import com.freezlex.kohanato.core.events.*
import com.freezlex.kohanato.core.i18n.Language
import com.freezlex.kohanato.core.i18n.LanguageModel
import com.freezlex.kohanato.core.throwable.CommandThrowable
import mu.KotlinLogging
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
/**
 * Used to create new instances of JDA's DefaultShardManagerBuilder implementation.
 * A single KohanatoCore cannot be reused multiple times. Each call to launch() initiate an instance using all the provided information.
 * @author Freezlex
 */

class KoListener(
    val core: KohanatoCore,
    val event: GenericEvent
) {

    var language: LanguageModel? = Language["en"];

    suspend fun run() {
        when(event){
            is ReadyEvent -> OnReadyEvent.run(this)
            is SlashCommandInteractionEvent -> OnSlashCommandEvent.run(this)
            is MessageReceivedEvent -> OnMessageReceivedEvent.run(this)
            is ButtonInteractionEvent -> OnButtonClickEvent.run(this)
            is UserContextInteractionEvent -> OnUserContextInteractionEvent.run(this)
            is MessageContextInteractionEvent -> OnMessageContextInteractionEvent.run(this)
        }
    }

    fun dispatchSafely(invoker: (CommandThrowable) -> Unit) {
        try {
            CommandThrowable.run(invoker)
        } catch (e: Throwable) {
            try {
                CommandThrowable.onInternalError(e)
            } catch (inner: Throwable) {
                println(inner)
            }
        }
    }
}

val logger = KotlinLogging.logger {}