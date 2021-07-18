package com.freezlex.jamesbot.internals.client

import com.freezlex.jamesbot.database.entities.*
import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.api.Subscription
import com.freezlex.jamesbot.internals.i18n.LanguageList
import com.freezlex.jamesbot.internals.api.Utility
import com.freezlex.jamesbot.internals.i18n.LanguageModel
import com.freezlex.jamesbot.internals.i18n.Languages
import com.freezlex.jamesbot.internals.indexer.Executable
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

object ClientCache {

    private val userCache: HashMap<Long, UserSettings> = HashMap()
    private val guildCache: HashMap<Long, GuildSettings> = HashMap()
    private val subscriptionCache: HashMap<Long, UserSubscription> = HashMap()
    val permissionCache: MutableList<CommandPermission> = mutableListOf()

    /*init {
        val scheduler = Timer()
        val task = TimerHelper()
        scheduler.schedule(task, 10000, 10000)
    }*/

    fun hasPermission(invoked: Executable, author: User, channel: GuildChannel, refresh: Boolean = false): Boolean {
        val command = invoked.name
        val category = invoked.properties.category().category

        var permission: CommandPermission? = this.permissionCache.find { it.reference == author.idLong && it.category.equals(category, true) && it.command.equals(command, true)}
        if(permission != null) return permission.isAuthorised

        // Check for role permission
        val member = channel.guild.getMember(author)
        permission = if(member != null) this.permissionCache.find { it.command.equals(command, true) && it.category.equals(category, true) && member.roles.any {role -> role.idLong == it.reference} } else null
        if(permission != null) return permission.isAuthorised

        // Check for channel permission
        permission = this.permissionCache.find { it.reference == channel.idLong && it.category.equals(category, true) && it.command.equals(command, true) }
        if(permission != null) return permission.isAuthorised

        // Check for guild permission
        permission = this.permissionCache.find { it.reference == channel.guild.idLong && it.category.equals(category, true) && it.command.equals(command, true) }
        if(permission != null) return permission.isAuthorised

    return if(refresh) refreshPermissionCache(invoked, author, channel) else true
    }

    fun checkSubscription(invoked: Executable, author: User, guild: Guild? ,refresh: Boolean = false): Boolean {
        var subscription: UserSubscription? = this.subscriptionCache[author.idLong]
        if(subscription == null)subscription = this.subscriptionCache[guild!!.ownerIdLong]
        if(subscription != null && invoked.properties.subscription().ordinal <= subscription.subscription){
            if(subscription.endDate != null && subscription.endDate!! < Instant.now().epochSecond) return if(refresh) refreshSubscriptionCache(invoked, author, guild) else false
            else invoked.properties.subscription().ordinal <= subscription.subscription
        }
        else if(){

        }
        return if(refresh) refreshSubscriptionCache(invoked, author, guild) else invoked.properties.subscription() <= Subscription.USER
    }

    private fun refreshSubscriptionCache(invoked: Executable, author: User, guild: Guild?): Boolean{
        // TODO : Create a refresh for the subscription cache
        var result = UsersSubscriptions.findOrNull(author)
        if(result != null)this.subscriptionCache[transaction { result!!.user.userId  }] = result
        if(guild != null){
            result = UsersSubscriptions.findOrNull(guild.owner!!.user)
            if(result != null)this.subscriptionCache[transaction { result.user.userId  }] = result
        }
        return checkSubscription(invoked, author, guild, refresh = false)
    }

    private fun refreshPermissionCache(invoked: Executable, author: User, channel: GuildChannel): Boolean{
        // TODO : Create a refresh for the permission cache
        return hasPermission(invoked, author, channel, false)
    }

    /**
     * Find the settings of a guild
     * @param guild The guild to find the cache or fetch the DB
     */
    fun getGuildSettingsOrCreate(guild: Guild) : GuildSettings{
        var cache = this.guildCache[guild.idLong]
        if(cache == null) {
            cache = GuildsSettings.findOrCreate(guild)
            this.guildCache[guild.idLong] = cache
        }
        return cache
    }

    /**
     * Find the settings of a user or create it if null
     * @param entity The user to find the cache or fetch the DB
     */
    fun getUserSettingsOrCreate(entity: User) : UserSettings{
        var cache = this.userCache[entity.idLong]
        if(cache == null) {
            cache = UsersSettings.findOrCreate(entity)
            // this.userCache[entity.idLong] = cache
        }
        return cache
    }

    /**
     * Find the settings of a user
     * @param entity The user to find the cache or fetch the DB
     */
    fun getUserSettings(entity: User) : UserSettings? {
        var cache = this.userCache[entity.idLong]
        if(cache == null){
            cache = UsersSettings.findOrNull(entity)
            if(cache != null) this.userCache[entity.idLong] = cache
        }
        return cache
    }
    /**
     * Get the guild prefix from the cached data or from the database if unavailable
     * @param jda The JDA client for the client id
     * @param guild The guild where the event is coming from
     */
    fun getPrefix(jda: JDA, guild: Guild): Regex{
        return genPattern(jda, getGuildSettingsOrCreate(guild).prefix)
    }

    fun getLanguage(user: User, guild: Guild?): String {
        val us = getUserSettings(user)
        return when{
            us != null -> us.regCde
            guild != null -> getGuildSettingsOrCreate(guild).regCde
            else -> ClientSettings.language
        }
    }

    fun resolveLanguage(user: User, guild: Guild?): LanguageModel {
        val lg = getLanguage(user, guild)
        return Languages[lg]?: Languages[ClientSettings.language]!!
    }

    /**
     * Pattern builder from message provider
     * @param jda The incoming JDA client
     * @return The build Regex pattern
     */
    private fun genPattern(jda: JDA, prefix: String): Regex {
        val escapedPrefix: String = Utility.escapeRegex(prefix);
        return Regex(
            "^(<@!?${jda.selfUser.id}>\\s+(?:${escapedPrefix}\\s*)?|${escapedPrefix})([A-z][^\\s]+)",
            RegexOption.IGNORE_CASE
        )
    }

    fun getPattern(ctx: Context): Pair<String, String>{
        val jda = ctx.messageContext?.jda?: ctx.slashContext!!.jda
        if(ctx.isFromGuild()){
            val settings = getGuildSettingsOrCreate(ctx.messageContext?.guild?: ctx.slashContext!!.guild!!)
            return Pair(settings.prefix, "<@!?${jda.selfUser.id}>")
        } else {
            return Pair(ClientSettings.prefix, "<@!?${jda.selfUser.id}>")
        }
    }

    fun refreshCache(){
        // TODO : Create a refresh for the cache
    }
}

class TimerHelper: TimerTask() {
    override fun run() {
        ClientCache.refreshCache()
    }
}
