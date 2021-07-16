package com.freezlex.jamesbot.internals.arguments

import net.dv8tion.jda.api.interactions.commands.OptionType

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Argument(
    val name: String = "",
    val options: Array<String> = [],
    val type: OptionType = OptionType.STRING,
    val greedy: Boolean = false,
    val tentative: Boolean = false,
    val description: String = "No description provided"
)
