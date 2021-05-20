package com.freezlex.jamesbot.database.entity

import com.freezlex.jamesbot.internals.models.GuildSettings
import org.hibernate.annotations.Type
import javax.persistence.*
import kotlin.math.max

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
){
    constructor(guildId: Long, owner: UserEntity): this(0, guildId, owner)
}

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
    var prefix: String = System.getenv("PREFIX")
){
    constructor(guild: GuildEntity, prefix: String): this(0, guild, prefix)
    constructor(guild: GuildEntity, guildSettings: GuildSettings): this(0, guild, guildSettings.prefix)
}


@Entity(name = "guild_permissions")
class GuildPermissionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @OneToOne
    val guild: GuildEntity,
    val role: Long,
    @Column( name = "discord_permission", length = 20)
    val discordPermission: Int,
    @Column(name = "level_permission")
    val levelPermission: Int
    )
