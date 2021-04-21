package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GuildRepository: JpaRepository<GuildEntity, Int>{
    fun findOneByGuildId(guildId: Long): Optional<GuildEntity>
}
