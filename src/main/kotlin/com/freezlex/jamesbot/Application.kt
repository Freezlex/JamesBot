package com.freezlex.jamesbot

import com.freezlex.jamesbot.internals.Launcher
import com.freezlex.jamesbot.internals.api.ClientSettings

/**
 * Main application launcher
 */
fun main() {
    ClientSettings.defineOwners(mutableListOf(306703362261254154)).processByEnv()
    Launcher();
}
