package com.freezlex.jamesbot.implementation.moderation

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.Permission
import java.util.concurrent.TimeUnit

class PrefixCommand: Cmd {
    override fun name(): String = "Prefix"
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun userPermissions() = listOf(Permission.ADMINISTRATOR)

    fun run(ctx: Context, prefix: String){

    }

}
