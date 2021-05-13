package com.freezlex.jamesbot.database.entity

import javax.persistence.*

/**
 * Guild settings entity for JpaRepository
 */
@Entity(name = "guilds_settings")
class GuildSettingsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @OneToOne
    val guild: GuildEntity,
    val prefix: String = System.getenv("PREFIX")
){
    constructor(guild: GuildEntity, prefix: String): this(0, guild, prefix)
}
