package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Guild JPA Repository
 */
interface GuildSettingsRepository: JpaRepository<GuildSettingsEntity, Int>{

    /**
     * Get a guild setting by the associated guild
     * @return GuildSettingsEntity
     */
    fun findByGuild(guild: GuildEntity): Optional<GuildSettingsEntity>
}
