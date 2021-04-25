package com.freezlex.jamesbot.database.entity

import javax.persistence.*

/**
 * Guild entity for JpaRepository
 */
@Entity(name = "guilds")
class GuildEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "guild_id")
    val guildId: Long,
    @ManyToOne
    val owner: UserEntity
)
