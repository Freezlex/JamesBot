package com.freezlex.kohanato.core.i18n

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*

class LangManager(private val lang: String) {
    /**
     * Get a [String] from the lang file
     * @param key [String] key
     * @param default [String] default value
     *
     * @return [String] value
     */
    fun getString(obj: Any, func: String, key: String, default: String): String = getString(LangKey.keyBuilder(obj, func, key).toString(), default)
    private fun getString(key: String, default: String): String {
        val langFile = LangFile.load(lang)

        fun createMissingKeys(): String {
            val missing = key.split(".")
            val newObject = langFile.data
            val checked: MutableList<String> = mutableListOf()
            var current = newObject
            missing.forEach {
                checked.add(it)
                current = if (current.has(it) && current.get(it) is JsonObject) current.getAsJsonObject(it)
                else {
                    if (missing.indexOf(it) == missing.size - 1) return@forEach current.addProperty(it, default)
                    current.add(it, JsonObject())
                    current.getAsJsonObject(it)
                }
            }
            langFile.data = newObject
            return default
        }

        val queue: LinkedList<String> = LinkedList(key.split("."))

        var currentElement: Any = langFile.data
        for (i in 0 until queue.size) {
            val current = queue.poll()
            if (currentElement !is JsonObject) continue
            if (currentElement.has(current)) {
                val element = currentElement.get(current)
                if (element is JsonObject && queue.isEmpty()) return createMissingKeys()
                else currentElement = element
            } else return createMissingKeys()
        }
        currentElement as JsonElement
        return currentElement.asString.ifEmpty { default }
    }
}