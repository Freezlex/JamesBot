package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GuildSettingsRepository: JpaRepository<GuildSettingsEntity, Int>{
    fun findByGuild(guild: GuildEntity): Optional<GuildSettingsEntity>
}
