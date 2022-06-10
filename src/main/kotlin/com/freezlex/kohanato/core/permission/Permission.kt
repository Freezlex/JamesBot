package com.freezlex.kohanato.core.permission

enum class Permission(val flag: Int) {
    ADMIN(0x1),
    MODERATOR(0x2),
    SUPPORT(0x3),
    READ_PERMISSIONS(0x4),
    WRITE_PERMISSIONS(0x5),
    DELETE_PERMISSIONS(0x6);
}