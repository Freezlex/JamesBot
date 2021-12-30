package com.freezlex.kohanato.api.contextual

interface BaseCommand {

    val category: String?
        get() = null
    val subSlashCommand: Boolean
        get() = true
}