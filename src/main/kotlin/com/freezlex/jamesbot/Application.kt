package com.freezlex.jamesbot

import com.freezlex.jamesbot.internals.Launcher
import com.freezlex.jamesbot.internals.client.ClientSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Main application launcher
 */
fun main() {
    ClientSettings.defineOwners(mutableListOf(306703362261254154)).processByEnv()
    Launcher();
}

val logger: Logger = LoggerFactory.getLogger("JamesBot")
