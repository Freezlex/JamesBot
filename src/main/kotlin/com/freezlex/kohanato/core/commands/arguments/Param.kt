package com.freezlex.kohanato.core.commands.arguments

import net.dv8tion.jda.api.interactions.commands.OptionType

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param(
    /**
     * The name of the command
     */
    val name: String = "",
    /**
     * Defined which arguments can be possibly provided for the command
     */
    val options: Array<String> = [],
    /**
     * The type of the argument (default STRING)
     */
    val type: OptionType = OptionType.STRING,
    /**
     *
     */
    val greedy: Boolean = false,
    /**
     *
     */
    val tentative: Boolean = false,
    /**
     * A short sentence that describe the command
     * @sample "A command to nuke the entire world ... (and maybe the guild)"
     */
    val description: String = "No description provided"
)
