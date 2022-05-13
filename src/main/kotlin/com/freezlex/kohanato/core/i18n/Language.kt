package com.freezlex.kohanato.core.i18n

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.logger
import com.google.gson.Gson
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.GenericEvent
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

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
        val code = getGuildLangCode() ?: "en"
        if (!LANG_MANAGERS.containsKey(code)) LANG_MANAGERS[code] = LangManager(code)
        return LANG_MANAGERS[code]!!
    }

    private fun getGuildLangCode(guild: Guild?): String? = guild?.locale?.toLanguageTag()?.split("-")?.first()
}
