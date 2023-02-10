package com.freezlex.kohanato.core.database

import com.freezlex.kohanato.core.logger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import kotlin.system.exitProcess

class Database {
    private val tables: List<Table> = listOf()

    init{
        // Get database config from ENVIRONMENT
        val config = DatabaseConfig(
            System.getenv("MYSQL_HOST") ?: "127.0.0.1",
            System.getenv("MYSQL_PORT") ?: "3306",
            System.getenv("MYSQL_DATABASE")
                ?: throw Exception("The database name must be defined as environment variable."),
            System.getenv("MYSQL_USER") ?: "root",
            System.getenv("MYSQL_PASSWORD")
                ?: throw Exception("The database password must be defined as environment variable."))

        connect(config)
        initTables()
    }

    private fun connect(databaseConfig: DatabaseConfig) { // Connect to database
        Database.connect(
            "jdbc:mysql://${databaseConfig.host}:${databaseConfig.port}/${databaseConfig.database}",
            "com.mysql.cj.jdbc.Driver",
            databaseConfig.user,
            databaseConfig.password)
        for(i in 0..5){
            try{
                transaction { connection.isClosed }
                break;
            }catch (e: Exception){
                if(i == 5){
                    logger.error("Unable to connect to the provided datasource, please check your credentials (try : $i/5)")
                    exitProcess(0)
                };
                logger.error("Unable to connect to the database retrying in 5s (try : $i/5)");
                logger.debug("${databaseConfig.host} ${databaseConfig.port} ${databaseConfig.database} ${databaseConfig.user} ${databaseConfig.password}")
                Thread.sleep(5000);
            }
        }
    }

    private fun initTables() {
        for (table in tables) {
            transaction {
                SchemaUtils.create(table)
            }
        }
    }
}
