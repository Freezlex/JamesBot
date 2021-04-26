package com.freezlex.jamesbot.implementation.commands

import com.freezlex.jamesbot.internals.commands.Command
import net.dv8tion.jda.api.Permission
import org.springframework.stereotype.Component

/**
 * Implementation for PING command
 */
@Component
class PingCommand(userPermission: List<Permission>?) : Command(
    "Ping",
    listOf("p"),
    null,
    null,
    false,
    null,
    listOf(Permission.MESSAGE_WRITE),
) {

    override fun run():Boolean{
        return true
    }
}
