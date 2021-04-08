package com.freezlex.jamesbot

import com.freezlex.jamesbot.internals.event.EventManagers
import com.freezlex.jamesbot.internals.utils.Settings

fun main() {
    Bot(Settings.BOT_TOKEN, listOf(EventManagers())).start();
}
