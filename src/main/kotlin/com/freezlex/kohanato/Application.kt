package com.freezlex.kohanato

import com.freezlex.kohanato.api.Listener
import dev.minn.jda.ktx.*
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.ExperimentalTime

/**
 * Main application launcher
 */
fun main() {
    DefaultShardManagerBuilder.createDefault(System.getenv("BOT_TOKEN"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
        .disableIntents(listOf(
        GatewayIntent.GUILD_MESSAGE_TYPING,
        GatewayIntent.DIRECT_MESSAGE_TYPING,
        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
    ))
        .addEventListeners(Listener())
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableCache(
            CacheFlag.VOICE_STATE,
            CacheFlag.ONLINE_STATUS,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.EMOTE,
            CacheFlag.MEMBER_OVERRIDES,
            CacheFlag.ROLE_TAGS
        ).injectKTX().build()
}

val logger: Logger = LoggerFactory.getLogger("Koahanto")
