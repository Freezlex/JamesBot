package com.freezlex.kohanato.core.cooldown

import com.freezlex.kohanato.core.commands.contextual.KoCommand
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.time.Duration

object CooldownProvider {
    private val buckets = ConcurrentHashMap<BucketType, Bucket>()

    fun isOnCooldown(id: Long, bucket: BucketType, koCommand: KoCommand): Boolean {
        return buckets[bucket]?.isOnCooldown(id, koCommand.name) ?: false
    }

    fun getCooldownTime(id: Long, bucket: BucketType, koCommand: KoCommand): Long {
        return buckets[bucket]?.getCooldownRemainingTime(id, koCommand.name) ?: 0
    }

    fun setCooldown(id: Long, bucket: BucketType, time: Duration, koCommand: KoCommand) {
        buckets.computeIfAbsent(bucket) { Bucket() }.setCooldown(id, time, koCommand.name)
    }

    class Bucket {
        private val sweeperThread = Executors.newSingleThreadScheduledExecutor()
        private val cooldowns = ConcurrentHashMap<Long, MutableSet<Cooldown>>() // EntityID => [Commands...]

        fun isOnCooldown(id: Long, commandName: String): Boolean {
            return getCooldownRemainingTime(id, commandName) > 0
        }

        fun getCooldownRemainingTime(id: Long, commandName: String): Long {
            val cd = cooldowns[id]?.firstOrNull { it.name == commandName }
                ?: return 0

            return abs(cd.expires - System.currentTimeMillis())
        }

        fun setCooldown(id: Long, time: Duration, commandName: String) {
            val d = time.inWholeMilliseconds
            val cds = cooldowns.computeIfAbsent(id) { mutableSetOf() }
            val cooldown = Cooldown(commandName, System.currentTimeMillis() + d)
            cds.add(cooldown)

            sweeperThread.schedule({ cds.remove(cooldown) }, d, TimeUnit.MILLISECONDS)
        }
    }

    class Cooldown(val name: String, val expires: Long) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Cooldown

            return name == other.name
        }

        override fun hashCode(): Int {
            return 31 * name.hashCode()
        }
    }
}