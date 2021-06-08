package com.freezlex.jamesbot.database

import com.freezlex.jamesbot.database.entities.Guild
import com.freezlex.jamesbot.database.entities.User
import com.freezlex.jamesbot.internals.api.ClientSettings
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

/** Database class */
class Database (private var clientSettings: ClientSettings){

    private val tables: List<Table> = listOf(User, Guild)

    init{
        connect()
        initTables()
    }

    private fun connect(){
        Database.connect(
            "jdbc:mysql://${clientSettings.dbHost}:${clientSettings.dbPort}/${clientSettings.dbName}",
            "com.mysql.cj.jdbc.Driver",
            clientSettings.dbUser,
            clientSettings.dbPassword)
    }

    private fun initTables(){
        for(table in tables){
            transaction {
                SchemaUtils.create(table)
            }
        }
    }

}
