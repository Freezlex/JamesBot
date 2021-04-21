package com.freezlex.jamesbot.database.entity

import javax.persistence.*

/**
 * User entity for JpaRepository
 */
@Entity
data class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "user_id", unique = true)
    val userId: Long,
    val username: String,
    val tag: String
    )
