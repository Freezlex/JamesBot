package com.freezlex.jamesbot

import com.freezlex.jamesbot.commands.CommandsRegistry
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag

class Bot (private val token: String, private val listeners: List<Any>){
    fun start(): ShardManager {

        val builder = DefaultShardManagerBuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(listeners);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.ROLE_TAGS ,CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
        builder.setActivity(Activity.competing("👶🍼 Kotlin ❤"))

        return builder.build();
    }
}
