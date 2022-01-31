package com.freezlex.kohanato.api.arguments

import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.reflect.KParameter

class Arguments (
    val name: String,
    val type: Class<*>,
    val greedy: Boolean,
    val optional: Boolean, // Denotes that a parameter has a default value.
    val isNullable: Boolean,
    val isTentative: Boolean,
    val slashType: OptionType,
    val description: String,
    val options: MutableList<String>,
    internal val kparam: KParameter
        ) {
    fun format(withType: Boolean): String {
        return buildString {
            if (optional || isNullable) {
                append('[')
            } else {
                append('<')
            }

            append(name)

            if (withType) {
                append(": ")
                append(type.simpleName)
            }

            if (optional || isNullable) {
                append(']')
            } else {
                append('>')
            }
        }
    }
}
