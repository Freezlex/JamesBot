package com.freezlex.jamesbot

import com.freezlex.jamesbot.internals.CommandsRegistry
import com.freezlex.jamesbot.managers.EventManagers
import com.freezlex.jamesbot.utils.Arguments
import com.freezlex.jamesbot.utils.CommandEvent
import com.freezlex.jamesbot.utils.Settings

fun main(args: Array<String>) {
    Bot(Settings.BOT_TOKEN, listOf(EventManagers())).start();
}
