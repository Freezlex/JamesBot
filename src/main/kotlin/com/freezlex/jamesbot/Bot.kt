package com.freezlex.jamesbot

import com.freezlex.jamesbot.internals.commands.CommandsRegistry
import com.freezlex.jamesbot.internals.event.EventRegistry
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Default Bot shard builder
 */
@Component
class Bot{

    @Autowired
    lateinit var eventRegistry: EventRegistry

    @Autowired
    lateinit var commandRegistry: CommandsRegistry

    /**
     * @return The sharded bot
     */
    fun start(): ShardManager {

        // Register all events listeners
        eventRegistry.loadListeners();
        commandRegistry.loadCommands();

        val defaultShardManagerBuilder = DefaultShardManagerBuilder
            .create(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
            .setToken(System.getenv("TOKEN") ?: "default_value")
            .setActivity(Activity.playing("👶🍼 Kotlin ❤"))
            .setStatus(OnlineStatus.ONLINE)
            .setAutoReconnect(true)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.NONE)
            .addEventListeners(eventRegistry.build())
            .build();
        return defaultShardManagerBuilder;
    }
}
