package com.freezlex.jamesbot.internals.api

/**
 * Utility tools
 */
object Utility {

    /**
     * Escape potential regex from a string
     * @param string
     *          The string to escape
     */
    fun escapeRegex(string: String): String{
        return string.replace(Regex("""[|\\{}()[\\]^$+*?.]""")) {m -> "\\${m.value}"}
    }
}
