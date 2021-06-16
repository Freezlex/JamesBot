package com.freezlex.jamesbot.internals.exceptions

import com.freezlex.jamesbot.internals.arguments.ArgumentEntity

class BadArgument(
    val argument: ArgumentEntity,
    val providedArgument: String,
    val original: Throwable? = null
) : Throwable("Argument `${argument.name}` must be a `${argument.type.simpleName}`")
