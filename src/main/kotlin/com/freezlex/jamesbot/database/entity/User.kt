package com.freezlex.jamesbot.database.entity

import javax.persistence.*

/**
 * User entity for JpaRepository
 */
@Entity(name = "users")
data class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "user_id", unique = true)
    val userId: Long,
    val username: String,
    val tag: String
    ){
    constructor(userId: Long, username: String, tag: String): this(0, userId, username, tag)
}

/**
 * User settings for JpaRepository
 */
@Entity(name = "users_settings")
data class UserSettingsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val language: String,
    @OneToOne
    val user: UserEntity
){
    constructor(language: String, user: UserEntity): this(0, language, user)
}
