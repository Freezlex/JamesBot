package com.freezlex.jamesbot.implementation.general

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission
import java.util.concurrent.TimeUnit

class PingCommand: Cmd {
    override fun name(): String = "Ping"
    override fun category() = CommandCategory.UTILITY
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun description() = "Une commande pour avoir le ping du bot"

    fun run(ctx: Context,
            @Argument(name="ping", description = "Le type de ping demandé", options = ["direct", "indirect"]) prefix: String){
        print("test")
    }
}
