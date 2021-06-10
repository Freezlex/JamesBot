package com.freezlex.jamesbot.internals.client

import com.freezlex.jamesbot.internals.api.exceptions.CommandEventAdapter
import com.freezlex.jamesbot.internals.api.exceptions.TestImplement
import com.freezlex.jamesbot.internals.arguments.ArgParser
import com.freezlex.jamesbot.internals.arguments.Snowflake
import com.freezlex.jamesbot.internals.arguments.parser.*
import com.freezlex.jamesbot.internals.commands.CommandRegistry
import com.freezlex.jamesbot.internals.entities.Emoji
import com.freezlex.jamesbot.internals.events.OnMessageReceivedEvent
import com.freezlex.jamesbot.internals.events.OnReadyEvent
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.net.URL
import kotlin.reflect.full.memberFunctions

class ExecutorClient(
    packageName: String
): EventListener {
    val commands : CommandRegistry = CommandRegistry()
    val clientCache : ClientCache = ClientCache()
    private val eventListener: MutableList<CommandEventAdapter> = mutableListOf()

    init {
        println("registering package")
        commands.register(packageName)
        eventListener.add(TestImplement())
        this.registerParsers()
        println("End registering package found ${commands.size} commands")
    }

    override fun onEvent(event: GenericEvent){
        try{
            when(event){
                is ReadyEvent -> OnReadyEvent.run(this, event)
                is MessageReceivedEvent -> OnMessageReceivedEvent.run(this, event)
            }
        }catch (e: Throwable){
            throw e
        }
    }

    fun dispatchSafely(invoker: (CommandEventAdapter) -> Unit) {
        try {
            println("Test 1")
            // TODO : Try to fix the invoker ... Idk how to call it ;-;
            eventListener.forEach(invoker)
        } catch (e: Throwable) {
            try {
                println("Test 2")
                eventListener.forEach { it.onInternalError(e)}
            } catch (inner: Throwable) {
                println("Test 3")
                println(inner)
            }
        }
    }

    private fun registerParsers() {
        // Kotlin types and primitives
        val booleanParser = BooleanParser()
        ArgParser.parsers[Boolean::class.java] = booleanParser
        ArgParser.parsers[java.lang.Boolean::class.java] = booleanParser

        val doubleParser = DoubleParser()
        ArgParser.parsers[Double::class.java] = doubleParser
        ArgParser.parsers[java.lang.Double::class.java] = doubleParser

        val floatParser = FloatParser()
        ArgParser.parsers[Float::class.java] = floatParser
        ArgParser.parsers[java.lang.Float::class.java] = floatParser

        val intParser = IntParser()
        ArgParser.parsers[Int::class.java] = intParser
        ArgParser.parsers[java.lang.Integer::class.java] = intParser

        val longParser = LongParser()
        ArgParser.parsers[Long::class.java] = longParser
        ArgParser.parsers[java.lang.Long::class.java] = longParser

        // JDA entities
        val inviteParser = InviteParser()
        ArgParser.parsers[Invite::class.java] = inviteParser
        ArgParser.parsers[net.dv8tion.jda.api.entities.Invite::class.java] = inviteParser

        ArgParser.parsers[Member::class.java] = MemberParser()
        ArgParser.parsers[Role::class.java] = RoleParser()
        ArgParser.parsers[TextChannel::class.java] = TextChannelParser()
        ArgParser.parsers[User::class.java] = UserParser()
        ArgParser.parsers[VoiceChannel::class.java] = VoiceChannelParser()

        // Custom entities
        ArgParser.parsers[Emoji::class.java] = EmojiParser()
        ArgParser.parsers[String::class.java] = StringParser()
        ArgParser.parsers[Snowflake::class.java] = SnowflakeParser()
        ArgParser.parsers[URL::class.java] = UrlParser()
    }
}
