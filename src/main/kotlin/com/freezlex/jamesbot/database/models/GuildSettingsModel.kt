package com.freezlex.jamesbot.database.models

import javax.persistence.*

/**
 * Guild settings constructor
 */
@Entity
class GuildSettingsModel constructor(
    @Id
    @OneToOne(cascade = [CascadeType.ALL])
    val guild:GuildsModel
)
