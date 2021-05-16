package com.freezlex.jamesbot.implementation.arguments

import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.models.ArgumentModel
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.entities.Member
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

class MemberType(
    override val type: String = "String",
    override val name: String,
    override val oneOf: List<Any>?,
    override val default: Any?,
    val unique: Boolean,
    private val pattern: Regex = Regex("^(?:<@!?)?([0-9]+)>?")
): Argument {

    constructor(name: String, oneOf: List<Any>?, default: Any?): this("Member", name, oneOf, default, true)

    constructor(name: String, unique: Boolean): this("Member", name, null, null, unique)

    override fun validate(message: MessageModel): ArgumentModel {
        // Find user by mention pattern
        if(this.pattern.matches(message.validateQueue[0])){
            val user = this.pattern.matchEntire(message.validateQueue[0])?.groupValues?.get(1)?.toLong()?.let { message.event.jda.getUserById(it) }
            if(user != null){
                return ArgumentModel(message.event.guild.getMember(user)!!, this);
            }else{
                throw Exception("No user found for ${message.validateQueue[0]}")
            }
        }
        val exactMatch = message.event.guild.members.find { m -> m.user.name.equals(message.validateQueue[0], true)
                || m.user.asTag.equals(message.validateQueue[0], true)
                || (m.nickname != null && m.nickname.equals(message.validateQueue[0], true))}
        if(exactMatch != null)return ArgumentModel(exactMatch, this)
        val nonExactMatch = message.event.guild.members.filter { m -> m.user.name.contains(message.validateQueue[0], true)}
        if(nonExactMatch.isNotEmpty()){
            if(nonExactMatch.size == 1)return ArgumentModel(nonExactMatch[0], this)
            if(this.unique)throw Exception("Multiple members found for `${message.validateQueue[0]}`, please be more specific. Found : `${nonExactMatch.joinToString("`, `") { m -> m.user.asTag }}`")
            return ArgumentModel(nonExactMatch, this)

        }else{
            throw Exception("No member found for `${message.validateQueue[0]}`")
        }
    }
}
