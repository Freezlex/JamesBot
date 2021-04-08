package com.freezlex.jamesbot.database.models

import javax.persistence.*

/**
 * User constructor
 */
@Entity
class UsersModel constructor(
    @Id
    @Column(name = "user_id")
    val userId: Long
)
