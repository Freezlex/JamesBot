package com.freezlex.jamesbot.database.models

import javax.persistence.*

/**
 * Guild constructor
 */
@Entity
class GuildsModel constructor(
    @Id
    @Column(unique = true, nullable = false)
    val guild_id:Long,
    @OneToOne(cascade = [CascadeType.ALL])
    val owner: UsersModel
)
