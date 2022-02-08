package com.freezlex.kohanato.core.commands.contextual

import com.freezlex.kohanato.core.commands.arguments.Argument
import com.freezlex.kohanato.core.cooldown.Cooldown
import com.freezlex.kohanato.core.indexer.Executable
import com.freezlex.kohanato.core.indexer.Jar
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class Command (
    name: String,
    val category: String,
    val jar: Jar?,
    val cooldown: List<Cooldown>?,
    method: KFunction<*>,
    baseCommand: BaseCommand,
    arguments: List<Argument>,
    kParameter: KParameter
    ) : Executable(name, method, baseCommand, arguments, kParameter)
