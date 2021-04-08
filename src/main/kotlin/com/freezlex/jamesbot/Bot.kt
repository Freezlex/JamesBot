package com.freezlex.jamesbot

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy

/**
 * Default Bot shard builder
 * @param token The bot token to connect to
 * @param listeners The Events listeners for the bot
 */
class Bot (private val token: String, private val listeners: List<Any>){

    /**
     * @return The sharded bot
     */
    fun start(): ShardManager {

        val defaultShardManagerBuilder = DefaultShardManagerBuilder
            .create(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
            .setToken(token)
            .setActivity(Activity.competing("👶🍼 Kotlin ❤"))
            .setStatus(OnlineStatus.IDLE)
            .setAutoReconnect(true)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.NONE)
            .addEventListeners(listeners)

        return defaultShardManagerBuilder.build();
    }
}
