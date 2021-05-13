package com.freezlex.jamesbot.internals.models

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

data class MessageModel(
    var pattern: MatchResult?,
    var event: GuildMessageReceivedEvent,
    var argsString: String,
    val validateQueue: MutableList<String>,
    var parsed: List<ArgumentModel>?
)
