package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.cooldown.Cooldown
import com.freezlex.jamesbot.internals.indexer.Executable
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.indexer.Jar
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class CommandFunction (
    name: String,
    val category: String,
    val jar: Jar?,
    val cooldown: Cooldown?,

    // Executable properties
    method: KFunction<*>,
    cmd: Cmd,
    arguments: List<Argument>,
    contextParameter: KParameter
) : Executable(name, method, cmd, arguments, contextParameter)
