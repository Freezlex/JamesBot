package com.freezlex.kohanato.api.extensions

import net.dv8tion.jda.api.JDA

/**
 * Command registry for Kohanato implemented directly into the JDA client
 */
val JDA.registry: HashMap<String, String>
    get() = this.registry