package com.freezlex.jamesbot.internals.api

object Utility {
    fun escapeRegex(string: String): String{
        return string.replace(Regex("""[|\\{}()[\\]^$+*?.]""")) {m -> "\\${m.value}"}
    }
}
