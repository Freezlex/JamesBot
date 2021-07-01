package com.freezlex.jamesbot.database.entities

import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

/** User entity for database transactional */
object UserEntities: IntIdTable("users") {
    val userId = long("user_id").uniqueIndex()
    val username = varchar("username", 40)
    val tag = varchar("tag", 4)

    fun findOrCreate(entity: User): UserEntity = transaction {UserEntity.find { userId eq entity.idLong }.firstOrNull()
        ?: UserEntity.new {
            userId = entity.idLong
            username = entity.name
            tag = entity.asTag.split('#')[1]
        }}
}

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserEntities)
    var userId by UserEntities.userId
    var username by UserEntities.username
    var tag by UserEntities.tag
}
