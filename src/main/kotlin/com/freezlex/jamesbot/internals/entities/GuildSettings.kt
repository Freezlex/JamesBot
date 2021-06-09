package com.freezlex.jamesbot.internals.entities

import com.freezlex.jamesbot.database.entities.Guild
import com.freezlex.jamesbot.database.entities.GuildSettings
import com.freezlex.jamesbot.database.entities.User
import com.freezlex.jamesbot.internals.api.Utility
import com.freezlex.jamesbot.internals.client.ClientSettings
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class GuildSettings (message: Message){
    var guildId: Long = message.guild.idLong
    var prefix: String = ClientSettings.prefix
    var lang: String = "en_EN"
    var pattern: Regex = genPattern()

    init {
        getSettings(message)
    }

    private fun getSettings(message: Message) {
        val guild: ResultRow? = transaction { (Guild innerJoin GuildSettings).select { Guild.guild_guildId eq guildId}.firstOrNull() }
        this.pattern = genPattern(message.jda)
        if(guild != null){
            this.prefix = guild[GuildSettings.settings_prefix]
            this.lang = guild[GuildSettings.settings_lang]
        }else{
            transaction {
                User.insertIgnore {
                    it[user_username] = message.author.name
                    it[user_userId] = message.author.idLong
                    it[user_tag] = message.author.asTag.split('#')[1]
                }
                Guild.insertIgnore {
                    it[guild_guildId] = message.guild.idLong
                    it[guild_name] = message.guild.name
                    it[guild_owner] = User.select { User.user_userId eq message.author.idLong }.first()[User.user_id]
                }
                GuildSettings.insertIgnore {
                    it[settings_guildId] = Guild.select { Guild.guild_guildId eq message.guild.idLong }.first()[Guild.guild_id]
                    it[settings_prefix] = prefix
                    it[settings_lang] = lang
                }
            }
        }
    }

    /**
     * Pattern builder from message provider
     * @param jda The incoming JDA client
     * @return The build Regex pattern
     */
    private fun genPattern(jda: JDA): Regex{
        val escapedPrefix: String = Utility.escapeRegex(this.prefix);
        this.pattern = Regex("^(<@!?${jda.selfUser.id}>\\s+(?:${escapedPrefix}\\s*)?|${escapedPrefix})([^\\s]+)", RegexOption.IGNORE_CASE)
        return this.pattern
    }

    /**
     * Default pattern builder
     * @return The build Regex pattern
     */
    private fun genPattern(): Regex{
        val escapedPrefix: String = Utility.escapeRegex(this.prefix);
        return Regex("^(${escapedPrefix})([^\\s]+)", RegexOption.IGNORE_CASE)
    }
}
