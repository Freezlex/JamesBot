package com.freezlex.kohanato.api.contextual

interface BaseCommand {

    val category: String
        get() = "Test"
    val subgroup: Boolean
        get() = true;
}