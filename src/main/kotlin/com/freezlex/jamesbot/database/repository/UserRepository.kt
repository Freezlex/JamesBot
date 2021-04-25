package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
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
    fun findOneByUserId(userId: Long): UserEntity?
}
