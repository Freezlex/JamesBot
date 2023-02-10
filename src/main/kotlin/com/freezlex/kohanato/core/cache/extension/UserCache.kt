package com.freezlex.kohanato.core.cache.extension

import com.freezlex.kohanato.core.database.models.UserEntity
import com.freezlex.kohanato.core.permission.Permission

data class UserCache(
    override var language: String = "en-EN",
    ) : ICache {
        constructor(user: UserEntity): this(user.Language)
    }
