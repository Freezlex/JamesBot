package com.freezlex.kohanato.api.indexer

import com.freezlex.kohanato.api.arguments.Arguments
import com.freezlex.kohanato.api.contextual.BaseCommand
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class Command (
    name: String,
    val category: String,
    val jar: Jar?,
    val cooldown: Int,
    method: KFunction<*>,
    private val baseCommand: BaseCommand,
    arguments: List<Arguments>,
    kParameter: KParameter
    ) : Executable(name, method, baseCommand, arguments, kParameter)
