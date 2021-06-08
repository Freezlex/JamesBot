package com.freezlex.jamesbot.internals

import com.freezlex.jamesbot.database.Database
import com.freezlex.jamesbot.internals.api.ExecutorClient
import com.freezlex.jamesbot.internals.api.ClientSettings
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy

class Launcher{

    init {
        Database(ClientSettings)
        this.buildLauncher().build()
    }

    private fun buildLauncher(): DefaultShardManagerBuilder {
        return DefaultShardManagerBuilder
            .create(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
            .setToken(ClientSettings.botToken)
            .setActivity(Activity.playing("👶🍼 Kotlin ❤"))
            .setStatus(OnlineStatus.ONLINE)
            .setAutoReconnect(true)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.NONE)
            .addEventListeners(ExecutorClient())
    }
}
