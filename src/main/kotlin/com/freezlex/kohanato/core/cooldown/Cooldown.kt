package com.freezlex.kohanato.core.cooldown

import kotlin.time.Duration

class Cooldown (
    /** The time unit of the duration. */
    val duration: Duration,
    /** The bucket this cool-down applies to. */
    val bucket: BucketType
        )