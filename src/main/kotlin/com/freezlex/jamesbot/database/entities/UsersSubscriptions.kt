package com.freezlex.jamesbot.database.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import java.time.Instant

object UsersSubscriptions: IntIdTable("users_subscription") {
    val user = reference("user_id", UserEntities).uniqueIndex()
    val subscription = integer("subscription_level").default(0) // 0 : USER, 1 : DONOR/BOOSTER , 2: PREMIUM , 3 : EARLY USER , 4 MASTER
    val startDate = long("start-date").default(Instant.now().epochSecond)
    val endDate = long("end-date").nullable()
}

class UserSubscription(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserSubscription>(UsersSubscriptions)
    var user by UserEntity referencedOn UsersSubscriptions.user
    val subscription by UsersSubscriptions.subscription
    val endDate by UsersSubscriptions.endDate
}
