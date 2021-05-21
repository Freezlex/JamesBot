package com.freezlex.jamesbot.database

import com.freezlex.jamesbot.internals.utils.ClientSettings
import org.jetbrains.exposed.sql.Database

/** Database class */
class Database (var clientSettings: ClientSettings){

    fun connect(){
        Database.connect(
            "jdbc:mysql://${clientSettings.dbHost}:${clientSettings.dbPort}/${clientSettings.dbName}?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
            "com.mysql.cj.jdbc.Driver",
            clientSettings.dbUser,
            clientSettings.dbPassword)
    }

}
