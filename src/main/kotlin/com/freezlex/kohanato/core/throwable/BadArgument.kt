package com.freezlex.kohanato.core.throwable

import com.freezlex.kohanato.core.commands.arguments.Argument

class BadArgument(
    argument: Argument,
    val providedArgument: String,
    val original: Throwable? = null
) : Throwable("Argument `${argument.name}` must be a `${argument.type.simpleName}`")