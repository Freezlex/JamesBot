package com.freezlex.jamesbot.internals.api

object ClientSettings {
    /** Bot config */
    var prefix: String = ".."
    lateinit var botToken: String
    /** Database config */
    var dbHost: String = "localhost"
    var dbPort: String = "3306"
    lateinit var dbName: String
    lateinit var dbUser: String
    lateinit var dbPassword: String
    private var owners: MutableList<Long>? = null

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
        if(ClientSettings.owners != null){
            ClientSettings.owners!!.addAll(owners)
        }else{
            ClientSettings.owners = owners
        }
        return this;
    }
}
