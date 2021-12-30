package com.freezlex.kohanato.api.extensions

import net.dv8tion.jda.api.JDA
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Command registry for Kohanato implemented directly into the JDA client
 */
val JDA.registry: HashMap<String, String>
    get() = this.registry

fun String.asDuration(): Duration? {
    val unit = this.takeLast(1)
    val duration = this.removeSuffix(unit).toInt()
    return when(unit.uppercase()){
        "D" -> duration.days
        "M" -> duration.minutes
        "S" -> duration.seconds
        "MS" -> duration.milliseconds
        else -> null
    }
}