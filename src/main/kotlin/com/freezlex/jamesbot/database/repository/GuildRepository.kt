package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Guild JPA Repository
 */
interface GuildRepository: JpaRepository<GuildEntity, Int>{

    /**
     * Get a guild by the discord ID
     * @return GuildEntity
     */
    fun findOneByGuildId(guildId: Long): GuildEntity?
}
