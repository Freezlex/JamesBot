package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.GuildSettingsEntity
import com.freezlex.jamesbot.database.entity.UserEntity
import com.freezlex.jamesbot.database.entity.UserSettingsEntity
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
@Repository
interface UserRepository: JpaRepository<UserEntity, Int>{

    /**
     * Get a user by the discord ID
     * @return UserEntity
     */
    fun findOneByUserId(userId: Long): Optional<UserEntity>
}

/**
 * User Settings JPA Repository
 */
@Repository
interface UserSettingsRepository: JpaRepository<UserSettingsEntity, Int>{
    fun findByUser_UserId(userId: Long): Optional<UserSettingsEntity>
}
