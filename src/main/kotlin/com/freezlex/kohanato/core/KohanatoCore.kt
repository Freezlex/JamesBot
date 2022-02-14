package com.freezlex.kohanato.core

import com.freezlex.kohanato.core.commands.KoCommands
import com.freezlex.kohanato.core.commands.parser.Parser
import com.freezlex.kohanato.core.events.*
import com.freezlex.kohanato.core.throwable.CommandThrowable
import dev.minn.jda.ktx.CoroutineEventListener
import dev.minn.jda.ktx.injectKTX
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Used to create new instances of JDA's DefaultShardManagerBuilder implementation.
 * A single KohanatoCore cannot be reused multiple times. Each call to launch() initiate an instance using all the provided information.
 * @author Freezlex
 */
class KohanatoCore: CoroutineEventListener {

    /**
     * Defining owner means that those users will bypass all the limits and perform low-level commands
     * @param owners Owners discord id's
     */
    fun defineOwners(vararg owners: Long): KohanatoCore{
        return this
    }

    fun processByEnv(): KohanatoCore{
        return this
    }

    fun commandsPackage(packageName: String): KohanatoCore{
        KoCommands
            .register(packageName)
        return this
    }

    fun launch(): KohanatoCore{
        Parser.init()
        DefaultShardManagerBuilder
            .createDefault(System.getenv("BOT_TOKEN"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
            .disableIntents(listOf(
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
            ))
            .setActivity(Activity.playing("Released soon 🔥"))
            .setStatus(OnlineStatus.ONLINE)
            .setAutoReconnect(true)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.NONE)
            .addEventListeners(this)
            .injectKTX().build()
        return this
    }

    override suspend fun onEvent(event: GenericEvent) {
        try{
            when(event){
                is ReadyEvent -> OnReadyEvent.run(event)
                is SlashCommandInteractionEvent -> OnSlashCommandEvent.run(this, event)
                is MessageReceivedEvent -> OnMessageReceivedEvent.run(this, event)
                is ButtonInteractionEvent -> OnButtonClickEvent.run(this, event)
                is UserContextInteractionEvent -> OnUserContextInteractionEvent.run(this, event)
                is MessageContextInteractionEvent -> OnMessageContextInteractionEvent.run(this, event)
            }
        }catch (e: Throwable){
            throw Throwable(e)
        }
    }

    fun dispatchSafely(invoker: (CommandThrowable) -> Unit) {
        try {
            CommandThrowable.run(invoker)
        } catch (e: Throwable) {
            try {
                CommandThrowable.onInternalError(e)
            } catch (inner: Throwable) {
                println(inner)
            }
        }
    }
}

val logger: Logger = LoggerFactory.getLogger("Kohanato")
