package com.freezlex.jamesbot.implementation.common

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.Subscription
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandCategory
import com.freezlex.jamesbot.internals.commands.CommandRegistry
import net.dv8tion.jda.api.EmbedBuilder
import java.util.*

class HelpCommand : Cmd {
    override fun name(): String = "Help"
    override fun description(): String = "Show a partial or a complete list of commands"
    override fun subscription(): Subscription = Subscription.USER

    fun run(ctx: Context, args: String?){
        if(args == null) showGlobalEmbed(ctx);
            else showCatOrCmdEmbed(ctx);
    }

    private fun showGlobalEmbed(ctx: Context) {
        val embed: EmbedBuilder = buildGlobalEmbed(ctx)
        embed.setDescription(ctx.language.category.common.help.embed.description!!.format()).clearFields()
        val categories = CommandRegistry.map { it.value.category }.distinct()
        for(c: CommandCategory in categories){
            val l =
            embed.addField(
                c.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                "`help ${c.category}`",
                true)
        }
    }

    private fun showCatOrCmdEmbed(ctx: Context){

    }

    fun buildGlobalEmbed(ctx: Context): EmbedBuilder {
        return EmbedBuilder(ctx.language.category.settings.language.embed)
    }
}
