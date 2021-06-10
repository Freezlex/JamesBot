package com.freezlex.jamesbot.implementation.test

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission
import java.util.concurrent.TimeUnit

class TestCommand: Cmd {

    override fun userPermissions(): List<Permission> = listOf()
    override fun botPermissions(): List<Permission> = listOf()

    override fun name() = "Test"
    override fun cooldown() = Cooldown(5, TimeUnit.SECONDS, BucketType.USER)

    fun run(ctx: Context, string: String){
        ctx.message.reply("You sent the argument $string")
    }
}
