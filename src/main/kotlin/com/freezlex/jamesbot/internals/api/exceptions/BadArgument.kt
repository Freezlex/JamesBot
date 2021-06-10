package com.freezlex.jamesbot.internals.api.exceptions

import com.freezlex.jamesbot.internals.arguments.Argument

class BadArgument(
    val argument: Argument,
    val providedArgument: String,
    val original: Throwable? = null
) : Throwable("`${argument.name}` must be a `${argument.type.simpleName}`")
