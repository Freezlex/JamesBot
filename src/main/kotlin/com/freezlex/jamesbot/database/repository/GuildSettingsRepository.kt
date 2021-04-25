package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Guild JPA Repository
 */
@Repository
interface GuildSettingsRepository: JpaRepository<GuildSettingsEntity, Int>{

    /**
     * Get a guild setting by the associated guild
     * @return GuildSettingsEntity
     */
    fun findByGuild(guild: GuildEntity?): GuildSettingsEntity?

    fun findByGuild_GuildId(guildId: Long): GuildSettingsEntity?
}
