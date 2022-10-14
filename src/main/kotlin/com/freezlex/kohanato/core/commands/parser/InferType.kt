package com.freezlex.kohanato.core.commands.parser

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.reflect.KType

object InferType: HashMap<OptionType, List<Any>>() {
    init {
        this[OptionType.STRING] = listOf(String::class)
        this[OptionType.INTEGER] = listOf(Int::class)
        this[OptionType.BOOLEAN] = listOf(Boolean::class)
        this[OptionType.USER] = listOf(User::class)
        this[OptionType.CHANNEL] = listOf(Channel::class)
        this[OptionType.ROLE] = listOf(Role::class)
        this[OptionType.NUMBER] = listOf(Double::class)
        this[OptionType.MENTIONABLE] = listOf(Member::class)
    }

    fun predicate(predictable: KType): OptionType{
        val t = this.filter { it.value.any { c -> c == predictable.classifier } }
        if(t.isEmpty()) return OptionType.STRING
        return t.keys.first()
    }
}