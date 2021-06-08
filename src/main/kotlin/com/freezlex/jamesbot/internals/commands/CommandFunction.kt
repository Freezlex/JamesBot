package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.api.Executable
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.api.Jar
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class CommandFunction (
    name: String,
    val category: String,
    val jar: Jar?,
    val properties: Cmd,

    // Executable properties
    method: KFunction<*>,
    cmd: Cmd,
    arguments: List<Argument>,
    contextParameter: KParameter
) : Executable(name, method, cmd, arguments, contextParameter)
