package com.freezlex.kohanato.core.cache.extension

import com.freezlex.kohanato.core.permission.Permission

data class UserCache(
    override var language: String,
    val permission: String
    ) : ICache