package com.freezlex.jamesbot.implementation.commands

import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.utils.CommandEvent
import org.springframework.stereotype.Component
import java.security.Permission

/**
 * Implementation for PING command
 */
@Component
class PingCommand : Command {
    override val name: String
        get() = "ping"
    override val alias: List<String>
        get() = listOf("p")
    override val description: String
        get() = "Ping"
    override val ownerOnly: Boolean
        get() = false
    override val userPermission: List<Permission>
        get() = listOf()
    override val botPermission: List<Permission>
        get() = listOf()

    override fun run(args: List<Argument>, event: CommandEvent): Any {
        return "Not Implemented yet"
    }
}
