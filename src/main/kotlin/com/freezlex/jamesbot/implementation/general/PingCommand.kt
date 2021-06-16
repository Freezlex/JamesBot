package com.freezlex.jamesbot.implementation.general

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission
import java.util.concurrent.TimeUnit

class PingCommand: Cmd {
    override fun name(): String = "Ping"
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun isSlash() = true

    fun run(ctx: Context, @Argument(options = ["direct", "indirect"]) prefix: String){
        print("test")
    }
}
