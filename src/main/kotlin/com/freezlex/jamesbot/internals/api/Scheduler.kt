package com.freezlex.jamesbot.internals.api

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Action Scheduler
 */
object Scheduler {
    private val schedule = Executors.newSingleThreadScheduledExecutor()

    /**
     * Schedule action every X ...
     * @param milliseconds
     *          The duration of the Scheduler
     * @param block
     *          The block to Schedule
     */
    fun every(milliseconds: Long, block: () -> Unit): ScheduledFuture<*> {
        return schedule.scheduleAtFixedRate(block, milliseconds, milliseconds, TimeUnit.MILLISECONDS)
    }
}
