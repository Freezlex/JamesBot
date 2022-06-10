package com.freezlex.kohanato.core.i18n

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.events.*
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.StatusChangeEvent
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.io.File

private val LANG_MANAGERS: MutableMap<String, LangManager> = mutableMapOf()

/**
 * Loads the lang files.
 */
object Language {

    init {
        if (LANG_MANAGERS.isEmpty()) {
            val folder = File("lang")
            folder.mkdirs()
            folder.listFiles()?.forEach {
                val name = it.name.split(".").first()
                LANG_MANAGERS[name] = LangManager(name)
            }
        }
    }

    /**
     * Get langManager by name.
     * @param user [User] user.
     * @param guild [Guild] guild.
     * @return [LangManager] langManager.
     */
    fun getLangManager(core: KoListener): LangManager {
        val code: String = when(core.event){
            is GenericInteractionCreateEvent -> getLangCodeFromInteraction(core.event as GenericInteractionCreateEvent)
            is GenericMessageEvent -> getLangCodeFromGeneric(core.event as GenericMessageEvent)
            is StatusChangeEvent -> getLangCodeFromGeneric(core.event as StatusChangeEvent)
            else -> "en";
        }
        if (!LANG_MANAGERS.containsKey(code)) LANG_MANAGERS[code] = LangManager(code)
        return LANG_MANAGERS[code]!!
    }

    /**
     * Determine lang code from cache or userLocal
     * @param event The event from the interaction event
     */
    private fun getLangCodeFromInteraction(event: GenericInteractionCreateEvent): String =
        event.userLocale.toLanguageTag().split("-").first()

    /**
     * Determine lang from cache or guild cache/locale
     * @param event The event from the generic message event
     */
    private fun getLangCodeFromGeneric(event: GenericMessageEvent): String =
        getGuildLangCode(event.guild)?: "en"

    private fun getLangCodeFromGeneric(event: StatusChangeEvent): String =
        "en"

    /**
     * TODO : Implement when cache is built
     * private fun getUserLangCode(user: User?): String?;
     */

    private fun getGuildLangCode(guild: Guild?): String? = guild?.locale?.toLanguageTag()?.split("-")?.first()
}
