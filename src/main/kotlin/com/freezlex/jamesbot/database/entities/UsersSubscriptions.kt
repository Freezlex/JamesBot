package com.freezlex.jamesbot.database.entities

import com.freezlex.jamesbot.internals.api.Subscription
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object UsersSubscriptions: IntIdTable("users_subscription") {
    val user = reference("user_id", UserEntities).uniqueIndex()
    val subscription = integer("subscription_level").default(0) // 0 : USER, 1 : DONOR/BOOSTER , 2: PREMIUM , 3 : EARLY USER , 4 MASTER
    val startDate = long("start-date").default(Instant.now().epochSecond)
    val endDate = long("end-date").nullable()

    fun findOrCreate(entity: User): UserSubscription {
        val result = UserEntities.findOrCreate(entity)
        return transaction {
            UserSubscription.find { user eq result.id}.firstOrNull()?:
            UserSubscription.new {
                user = result
            }
        }
    }

    fun findOrNull(entity: User): UserSubscription? {
        val result = UserEntities.findOrCreate(entity)
        return transaction { UserSubscription.find { user eq result.id}.firstOrNull() }
    }
}

class UserSubscription(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserSubscription>(UsersSubscriptions)
    var user by UserEntity referencedOn UsersSubscriptions.user
    val subscription by UsersSubscriptions.subscription
    val endDate by UsersSubscriptions.endDate
}
