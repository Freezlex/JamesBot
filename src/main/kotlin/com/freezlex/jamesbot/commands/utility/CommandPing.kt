package com.freezlex.jamesbot.commands.utility

import com.freezlex.jamesbot.commands.Command
import com.freezlex.jamesbot.utils.ChatUtil
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.function.Consumer

class CommandPing: Command(name = "ping") {

    override fun execute(args: List<String>, e: MessageReceivedEvent) {
        var time = System.currentTimeMillis()
        e.reply("Pinging...", Consumer {
            time = (System.currentTimeMillis() - time) / 2
            ChatUtil.edit(it, "**Ping:** ${time}ms")
        })
    }

    override fun onEvent(p0: GenericEvent) {
        TODO("Not yet implemented")
    }
}
