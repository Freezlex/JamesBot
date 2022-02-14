package com.freezlex.kohanato.core.indexer

import com.freezlex.kohanato.core.commands.arguments.Argument
import com.freezlex.kohanato.core.commands.contextual.BaseCommand
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.concurrent.ExecutorService
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.instanceParameter

abstract class Executable (
    var name: String,
    private val method: KFunction<*>,
    val command: BaseCommand,
    val arguments: List<Argument>,
    private val kParameter: KParameter,
        ) {
    suspend fun execute(event: GenericCommandInteractionEvent, args: HashMap<KParameter, Any?>, complete: (Boolean, Throwable?) -> Unit, executor: ExecutorService?) {
        method.instanceParameter?.let {
            args[it] = command }
        args[kParameter] = event

        if (method.isSuspend) {
            executeAsync(args, complete)
        } else {
            if (executor != null) {
                executor.execute {
                    executeSync(args, complete)
                }
            } else {
                executeSync(args, complete)
            }
        }
    }

    /**
     * Calls the related method with the given args.
     */
    private fun executeSync(args: HashMap<KParameter, Any?>, complete: (Boolean, Throwable?) -> Unit) {
        try {
            method.callBy(args)
            complete(true, null)
        } catch (e: Throwable) {
            complete(false, e.cause ?: e)
        }
    }

    /**
     * Calls the related method with the given args, except in an async manner.
     */
    private suspend fun executeAsync(args: HashMap<KParameter, Any?>, complete: (Boolean, Throwable?) -> Unit) {
        try {
            // TODO : Fix parser for Duration? which will not work until you mark it as non-nullable
            method.callSuspendBy(args)
            complete(true, null)
        } catch (e: Throwable) {
            complete(false, e.cause ?: e)
        }
    }
}
