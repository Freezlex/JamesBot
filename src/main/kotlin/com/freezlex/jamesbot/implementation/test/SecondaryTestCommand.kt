package com.freezlex.jamesbot.implementation.test

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.util.concurrent.TimeUnit

class SecondaryTestCommand: Cmd {

    override fun name() = "secondary"
    override fun cooldown() = Cooldown(5, TimeUnit.SECONDS, BucketType.USER)
    override fun category(): CommandCategory  = CommandCategory.UNCATEGORIZED

    fun run(ctx: Context, @Argument(type = OptionType.STRING) string: String){
        ctx.message.reply("You sent the argument $string of type : `${string::class.simpleName}`.").queue()
    }
}
