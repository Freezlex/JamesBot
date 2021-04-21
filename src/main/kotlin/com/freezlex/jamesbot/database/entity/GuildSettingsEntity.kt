package com.freezlex.jamesbot.database.entity

import javax.persistence.*

/**
 * Guild settings entity for JpaRepository
 */
@Entity
class GuildSettingsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @OneToOne
    val guild: GuildEntity,
    val prefix: String
)
