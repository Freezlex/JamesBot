package com.freezlex.jamesbot.database.repository

import com.freezlex.jamesbot.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<UserEntity, Int>{
    fun findOneByUserId(userId: Long): Optional<UserEntity>
}
