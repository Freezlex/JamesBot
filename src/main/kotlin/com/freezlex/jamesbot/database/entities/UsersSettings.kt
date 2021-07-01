package com.freezlex.jamesbot.database.entities

import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

/** User Settings entity for database transactional */
object UsersSettings: IntIdTable("users_settings") {
    val user = reference("user", UserEntities).uniqueIndex()
    val regCde = varchar("regional_code", 10)

    fun findOrCreate(entity: User): UserSettings {
        val result = UserEntities.findOrCreate(entity)
        return transaction {
            UserSettings.find { user eq result.id}.firstOrNull()?:
            UserSettings.new {
                user = result
                regCde = "en_EN"
            }
        }
    }

    fun findOrNull(entity: User): UserSettings? {
        val user = UserEntities.findOrCreate(entity)
        return transaction { UserSettings.findById(user.id) }
    }
}

class UserSettings(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserSettings>(UsersSettings)
    var user by UserEntity referencedOn UsersSettings.user
    var regCde by UsersSettings.regCde

}
