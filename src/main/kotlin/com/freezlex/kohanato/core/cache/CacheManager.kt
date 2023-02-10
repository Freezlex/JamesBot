package com.freezlex.kohanato.core.cache

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.cache.extension.GuildCache
import com.freezlex.kohanato.core.cache.extension.ICache
import com.freezlex.kohanato.core.cache.extension.UserCache
import com.freezlex.kohanato.core.database.models.GuildEntity
import com.freezlex.kohanato.core.database.models.UserEntities
import com.freezlex.kohanato.core.database.models.UserEntity
import io.github.reactivecircus.cache4k.Cache
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionScope
import kotlin.time.Duration.Companion.minutes

class CacheManager {

    private val cache = Cache.Builder().expireAfterWrite(10.minutes).build<String, ICache>()

    private fun initCache(user: User): UserCache{
        val data = transaction { UserEntity.find( UserEntities.DiscordId eq user.id.toLong()).firstOrNull() }
        val cache = if(data == null) return UserCache() else UserCache(data)
        this.cache.put(user.id, cache);
        return cache;
    }

    private fun initCache(guild: Guild): GuildCache{
        val data = transaction { GuildEntity.find( UserEntities.DiscordId eq guild.id.toLong()).firstOrNull() }
        val cache = if(data == null) return GuildCache() else GuildCache(data)
        this.cache.put(guild.id, cache);
        return cache;
    }

    fun get(user: User): ICache {
        val cache: ICache? = cache.get(user.id);
        return if(cache === null) initCache(user) else cache;
    }

    fun get(guild: Guild): ICache {
        val cache: ICache? = cache.get(guild.id);
        return if(cache === null) initCache(guild) else cache;
    }

}
