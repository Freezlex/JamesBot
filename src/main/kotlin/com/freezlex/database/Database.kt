package com.freezlex.database

import org.jetbrains.exposed.sql.Database

/** Database class */
class Database {
    companion object{
        /** Open connection to the database */
        val connect = Database.connect(
            "jdbc:mysql://localhost:3306/${System.getenv("DB_NAME")}?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
            "com.mysql.cj.jdbc.Driver",
            System.getenv("DB_USER"),
            System.getenv("DB_PASSWORD"))
    }
}
