package com.freezlex.kohanato.core.permission

enum class Permission(val flag: Int) {
    ADMIN(0x1),
    DEVELOPER(0x2),
    MODERATOR(0x4),
    SUPPORT(0x8),
}