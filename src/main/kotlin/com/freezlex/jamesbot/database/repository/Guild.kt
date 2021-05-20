package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.GuildPermissionEntity
import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Guild JPA Repository
 */
interface GuildRepository: JpaRepository<GuildEntity, Int>{

    /**
     * Get a guild by the discord ID
     * @return GuildEntity
     */
    fun findOneByGuildId(guildId: Long): Optional<GuildEntity>
}

/**
 * Guild JPA Repository
 */
@Repository
interface GuildSettingsRepository: JpaRepository<GuildSettingsEntity, Int>{

    /**
     * Get a guild setting by the associated guild
     * @return GuildSettingsEntity
     */
    fun findByGuild(guild: GuildEntity?): Optional<GuildSettingsEntity>

    fun findByGuild_GuildId(guildId: Long): Optional<GuildSettingsEntity>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE guilds_settings SET prefix = :prefix WHERE guild_id = :guildId", nativeQuery = true)
    fun saveGuildSettingsOrUpdateIfNotNull(
        @Param("guildId") guildId: Int,
        @Param("prefix") prefix: String)
}

@Repository
interface GuildPermissionsRepository: JpaRepository<GuildPermissionEntity, Int>{
    fun findByGuild_GuildId(guildId: Long): Optional<GuildPermissionsRepository>
}
