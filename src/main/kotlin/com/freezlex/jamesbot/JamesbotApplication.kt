package com.freezlex.jamesbot

import com.freezlex.jamesbot.managers.EventManagers
import com.freezlex.jamesbot.utils.Settings
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JamesbotApplication

fun main(args: Array<String>) {
	runApplication<JamesbotApplication>(*args)
	Bot(Settings.BOT_TOKEN, listOf(EventManagers())).start();
}
