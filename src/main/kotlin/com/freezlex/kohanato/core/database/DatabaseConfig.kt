package com.freezlex.kohanato.core.database

data class DatabaseConfig(
    val host: String,
    val port: String,
    val database: String,
    val user: String,
    val password: String,
)
