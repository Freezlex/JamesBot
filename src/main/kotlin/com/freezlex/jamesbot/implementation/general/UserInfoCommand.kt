package com.freezlex.jamesbot.implementation.general

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.cooldown.BucketType
import com.freezlex.jamesbot.internals.cooldown.Cooldown
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.util.concurrent.TimeUnit

class UserInfoCommand: Cmd {
    override fun name(): String = "User-Info"
    override fun aliases() = mutableListOf("ui", "user")
    override fun category() = CommandCategory.UTILITY
    override fun cooldown() = Cooldown(10, TimeUnit.SECONDS, BucketType.GUILD)
    override fun description() = "Une commande pour avoir les infos d'un utilisateur"

    fun run(ctx: Context, @Argument(name="Utilisateur", description = "L'utilisateur que vous voulez stalk", type = OptionType.USER) user: User){
    }
}
