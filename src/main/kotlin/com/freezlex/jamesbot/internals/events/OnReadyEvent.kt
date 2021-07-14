package com.freezlex.jamesbot.internals.events

import com.freezlex.jamesbot.database.entities.CommandPermission
import com.freezlex.jamesbot.database.entities.CommandsPermissions
import com.freezlex.jamesbot.internals.client.ClientCache
import com.freezlex.jamesbot.internals.client.ClientSettings
import com.freezlex.jamesbot.internals.client.ExecutorClient
import com.freezlex.jamesbot.internals.commands.CommandRegistry
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.events.ReadyEvent
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

object OnReadyEvent {
    fun run(executor: ExecutorClient, event: ReadyEvent){
        executor.commands.createSlash(event.jda, true)
        logger.info("${event.jda.selfUser.name} is ready (id: ${event.jda.selfUser.id})")

        transaction {
            val permission = CommandPermission.find { CommandsPermissions.reference eq 836318164517519401 }.firstOrNull()?: CommandPermission.new {
                reference = 563712774044123158
                command = "language"
                category = "settings"
                isAuthorised = false
            }

            ClientCache.permissionCache.add(permission)
        }

        ClientSettings.defineEarlyUsers(mutableListOf(306703362261254154))
    }
}
