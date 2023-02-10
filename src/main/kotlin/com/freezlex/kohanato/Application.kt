package com.freezlex.kohanato

import com.freezlex.kohanato.core.KohanatoCore
import mu.KotlinLogging

/**
 * Main application launcher
 */
fun main() {
    KohanatoCore()
        .defineOwners(306703362261254154)
        .commandsPackage("com.freezlex.kohanato.implementation")
        .processByEnv()
        .launch()
}
