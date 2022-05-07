package com.freezlex.kohanato.core.commands.contextual

import com.freezlex.kohanato.core.commands.Categories
import com.freezlex.kohanato.core.commands.arguments.Argument
import com.freezlex.kohanato.core.cooldown.Cooldown
import com.freezlex.kohanato.core.indexer.Executable
import com.freezlex.kohanato.core.indexer.Jar
import net.dv8tion.jda.api.events.GenericEvent
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class KoCommand (
    name: String,
    val category: Categories,
    val jar: Jar?,
    val cooldown: List<Cooldown>?,
    method: KFunction<*>,
    baseCommand: BaseCommand,
    arguments: List<Argument>,
    kParameter: KParameter,
    event: KParameter
    ) : Executable(name, method, baseCommand, arguments, kParameter, event)
