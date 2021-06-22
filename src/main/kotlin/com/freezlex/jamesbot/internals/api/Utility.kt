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

    /**
     * Simple levenshtein method to compare two strings
     * @param tc List of CharSequence ton compare to cp
     * @param cp CharSequence as comparison point
     */
    fun findBestMatch(tc: List<CharSequence>, cp: CharSequence) : List<String> {

        val temp = HashMap<String, Int>() // HashMap<Command Name, Matching factor>

        for(c in tc){
            val cLength = c.length + 1
            val cpLength = cp.length + 1

            var cost = Array(cLength) { it }
            var newCost = Array(cLength) { 0 }

            for (i in 1 until cpLength) {
                newCost[0] = i

                for (j in 1 until cLength) {
                    val match = if(c[j - 1] == cp[i - 1]) 0 else 1

                    val costReplace = cost[j - 1] + match
                    val costInsert = cost[j] + 1
                    val costDelete = newCost[j - 1] + 1

                    newCost[j] = costInsert.coerceAtMost(costDelete).coerceAtMost(costReplace)
                }

                val swap = cost
                cost = newCost
                newCost = swap
            }

            if(cost[cLength - 1] != c.length) temp[c.toString()] = (c.length - cost[cLength - 1])
        }

        return temp.filter { it.value == temp.values.maxOrNull() }.map { it.key }
    }
}
