package com.freezlex.jamesbot.internals.models

import net.dv8tion.jda.api.events.Event

data class MessageModel(
    var pattern: MatchResult?,
    var event: Event,
    var argsString: String,
    var argsList: MutableList<String>,
    var parsed: List<ArgumentModel>?
)
