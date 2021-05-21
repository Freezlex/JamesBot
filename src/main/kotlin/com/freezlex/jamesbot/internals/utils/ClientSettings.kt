package com.freezlex.jamesbot.internals.utils

class ClientSettings {
    /** Bot config */
    var prefix: String = ".."
    lateinit var botToken: String
    /** Database config */
    var dbHost: String = "localhost"
    var dbPort: String = "3306"
    lateinit var dbName: String
    lateinit var dbUser: String
    lateinit var dbPassword: String

    fun processByEnv(): ClientSettings{
        this.prefix = System.getenv("PREFIX")?: this.prefix
        this.botToken = System.getenv("BOT_TOKEN")?: throw Exception("The bot token must be defined as environment variable.")
        this.dbHost = System.getenv("DB_HOST")?: this.dbHost
        this.dbPort = System.getenv("DB_PORT")?: this.dbPort
        this.dbName = System.getenv("DB_NAME")?: throw Exception("The database name must be defined as environment variable.")
        this.dbUser = System.getenv("DB_USER")?: throw Exception("The database user must be defined as environment variable.")
        this.dbPassword = System.getenv("DB_PASSWORD")?: throw Exception("The database password must be defined as environment variable.")
        return this
    }
}
