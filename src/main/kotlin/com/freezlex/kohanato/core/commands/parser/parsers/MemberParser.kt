package com.freezlex.kohanato.core.commands.parser.parsers

import com.freezlex.kohanato.core.commands.parser.Parsed
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import java.util.*

/**
 * The custom parser for the Member type
 */
class MemberParser : Parsed<Member> {

    // TODO: Check ctx.message.mentionedMembers
    /**
     * Parse the argument
     * @param event
     *          The event
     * @param param
     *          The params to parse
     */
    override fun parse(event: GenericCommandInteractionEvent, param: String): Optional<Member> {
        val snowflake = snowflakeParser.parse(event, param)

        val member: Member? = if (snowflake.isPresent) event.guild!!.getMemberById(snowflake.get().resolved) else {
            if (param.length > 5 && param[param.length - 5].toString() == "#") {
                val tag = param.split("#")
                event.guild?.memberCache?.find { it.user.name == tag[0] && it.user.discriminator == tag[1] }
            } else {
                event.guild?.getMembersByName(param, false)?.firstOrNull()
            }
        }
        return Optional.ofNullable(member)
    }

    companion object {
        val snowflakeParser = SnowflakeParser() // We can reuse this
    }

}