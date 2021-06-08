package com.freezlex.jamesbot.internals.api

import java.util.concurrent.TimeUnit

class Cooldown (
    /** How long the cool-down lasts. */
    val duration: Long,
    /** The time unit of the duration. */
    val timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    /** The bucket this cool-down applies to. */
    val bucket: BucketType
)
