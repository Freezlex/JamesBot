package com.freezlex.jamesbot.internals.client

import com.freezlex.jamesbot.internals.i18n.LanguageList

object ClientSettings {
    /** Bot config */
    var prefix: String = ".."
    lateinit var botToken: String
    /** Database config */
    var dbHost: String = "127.0.0.1"
    var dbPort: String = "3306"
    const val language: String = "en_EN"
    lateinit var dbName: String
    lateinit var dbUser: String
    lateinit var dbPassword: String
    private val owners: MutableList<Long> = mutableListOf()
    private val earlyUsers: MutableList<Long> = mutableListOf()

    fun processByEnv(): ClientSettings {
        prefix = System.getenv("PREFIX")?: prefix
        botToken = System.getenv("BOT_TOKEN")?: throw Exception("The bot token must be defined as environment variable.")
        dbHost = System.getenv("DB_HOST")?: dbHost
        dbPort = System.getenv("DB_PORT")?: dbPort
        dbName = System.getenv("DB_NAME")?: throw Exception("The database name must be defined as environment variable.")
        dbUser = System.getenv("DB_USER")?: throw Exception("The database user must be defined as environment variable.")
        dbPassword = System.getenv("DB_PASSWORD")?: throw Exception("The database password must be defined as environment variable.")
        return this
    }

    fun defineOwners(owners: MutableList<Long>): ClientSettings {
        this.owners.addAll(owners)
        return this
    }

    fun getOwners(): List<Long>{
        return this.owners
    }

    fun defineEarlyUsers(users: MutableList<Long>): ClientSettings {
        this.earlyUsers.addAll(users)
        return this
    }

    fun getEarlyUsers(): List<Long>{
        return this.earlyUsers
    }
}
