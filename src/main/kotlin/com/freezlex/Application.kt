package com.freezlex

import com.freezlex.database.Database
import com.freezlex.database.entities.User
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Main application launcher
 */
fun main() {
    Database.connect

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
