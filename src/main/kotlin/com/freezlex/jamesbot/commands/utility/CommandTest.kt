package com.freezlex.jamesbot.commands.utility

import com.freezlex.jamesbot.commands.Command
import com.freezlex.jamesbot.utils.ChatUtil
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.function.Consumer

class CommandTest: Command(name = "test") {

    override fun execute(args: List<String>, e: MessageReceivedEvent) {
        e.reply("You're running JamesBot 👌")
    }

    override fun onEvent(p0: GenericEvent) {
        TODO("Not yet implemented")
    }
}
