package com.freezlex.jamesbot.internals.arguments

import com.freezlex.jamesbot.internals.api.CommandContext
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.SlashContext
import java.util.*

interface Parser<T> {
    fun parse(ctx: CommandContext, param: String): Optional<T>
    fun parse(ctx: SlashContext, param: String): Optional<T> {
        TODO("Not yet implemented")
    }
}
