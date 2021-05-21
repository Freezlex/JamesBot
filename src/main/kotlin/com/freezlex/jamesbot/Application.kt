package com.freezlex.jamesbot

import com.freezlex.jamesbot.database.Database
import com.freezlex.jamesbot.database.entities.User
import com.freezlex.jamesbot.internals.utils.ClientSettings
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Main application launcher
 */
fun main() {
    val clientSettings = ClientSettings().processByEnv()
    Database(clientSettings).connect()

    val users: Query = transaction {
        User.insert {
            it[username] = "Freezlex"
            it[tag] = "0001"
            it[usrId] = 123456789012345678
        }
        User.selectAll()
    }

    users.forEach { println(it.toString()) }
}
