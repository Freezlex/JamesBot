package com.freezlex.jamesbot.database.entity

import javax.persistence.*

@Entity
class GuildSettingsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @OneToOne
    val guild: GuildEntity,
    val prefix: String
)
