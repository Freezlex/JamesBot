package com.freezlex.kohanato.core.database.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object SubscriptionEntities: IntIdTable("subscriptions") {
    val user = reference("user", UserEntities)
    val guild = reference("guild", GuildEntities)
    val subscription = integer("subscription").default(0)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val expire = datetime("expire_at")
}

class SubscriptionEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<SubscriptionEntity>(SubscriptionEntities)

    var user by SubscriptionEntities.user
    var guild by SubscriptionEntities.guild
    var subscription by SubscriptionEntities.subscription
    var createdAt by SubscriptionEntities.createdAt
    var expire by SubscriptionEntities.expire
}