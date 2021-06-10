package com.freezlex.jamesbot.internals.arguments

import com.freezlex.jamesbot.internals.api.Context
import java.util.*

interface Parser<T> {
    fun parse(ctx: Context, param: String): Optional<T>
}
