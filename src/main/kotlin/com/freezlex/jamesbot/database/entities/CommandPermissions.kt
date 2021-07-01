package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/** Guild entity for database transactional */
object CommandsPermissions: IntIdTable("commands_permissions") {
    val reference = long("reference").uniqueIndex() // guild, channel, role, user
    val command = varchar("command", 255) // command name
    val category = varchar("category", 255) // command category name
    val isAuthorised = bool("is_authorized") // isAuthorised
}

class CommandPermission(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<CommandPermission>(CommandsPermissions)
    var reference by CommandsPermissions.reference
    var command by CommandsPermissions.command
    var isAuthorised by CommandsPermissions.isAuthorised
    var category by CommandsPermissions.category
}
