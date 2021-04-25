package com.freezlex.jamesbot.internals.utils

object Utility {
    fun escapeRegex(string: String): String{
        return string.replace(Regex("""[|\\{}()[\\]^$+*?.]""")) {m -> "\\${m.value}"}
    }
}
