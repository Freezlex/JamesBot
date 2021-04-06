package com.freezlex.jamesbot.commands.implementation

import com.freezlex.jamesbot.commands.Command
import com.freezlex.jamesbot.utils.Arguments
import com.freezlex.jamesbot.utils.CommandEvent
import java.security.Permission

class Ping : Command{
    override val name: String
        get() = "ping"
    override val alias: List<String>
        get() = listOf("p")
    override val description: String
        get() = TODO("Not yet implemented")
    override val ownerOnly: Boolean
        get() = TODO("Not yet implemented")
    override val userPermission: List<Permission>
        get() = TODO("Not yet implemented")
    override val botPermission: List<Permission>
        get() = TODO("Not yet implemented")

    override fun run(args: List<Arguments>, event: CommandEvent): Any {
        println("khskdfqs");
        return "Wow";
    }
}
