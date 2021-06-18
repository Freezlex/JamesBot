package com.freezlex.jamesbot.internals.indexer

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.ArgumentEntity
import com.freezlex.jamesbot.internals.commands.Cmd
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.instanceParameter

abstract class Executable (
    var name: String,
    val method: KFunction<*>,
    val properties: Cmd,
    val arguments: List<ArgumentEntity>,
    private val contextParameter: KParameter
) {

    open fun execute(ctx: Context, args: HashMap<KParameter, Any?>, complete: (Boolean, Throwable?) -> Unit, executor: ExecutorService?) {
        method.instanceParameter?.let { args[it] = properties }
        args[contextParameter] = ctx

        if (method.isSuspend) {
            GlobalScope.launch {
                executeAsync(args, complete)
            }
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
            method.callSuspendBy(args)
            complete(true, null)
        } catch (e: Throwable) {
            complete(false, e.cause ?: e)
        }
    }

}
