package com.freezlex.jamesbot.internals.arguments

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.parser.*
import com.freezlex.jamesbot.internals.entities.Emoji
import com.freezlex.jamesbot.internals.exceptions.BadArgument
import com.freezlex.jamesbot.internals.exceptions.ParserNotRegistered
import com.freezlex.jamesbot.internals.indexer.Executable
import net.dv8tion.jda.api.entities.*
import java.net.URL
import java.util.*
import kotlin.reflect.KParameter

class ArgParser(
    private val ctx: Context,
    private val delimiter: Char,
    commandArgs: List<String>
) {
    private val delimiterStr = delimiter.toString()
    private var args = commandArgs.toMutableList()

    private fun take(amount: Int) = args.take(amount).onEach { args.removeAt(0) }
    private fun restore(argList: List<String>) = args.addAll(0, argList)

    private fun parseQuoted(): Pair<String, List<String>> {
        val iterator = args.joinToString(delimiterStr).iterator()
        val original = StringBuilder()
        val argument = StringBuilder("\"")
        var quoting = false
        var escaping = false

        loop@ while (iterator.hasNext()) {
            val char = iterator.nextChar()
            original.append(char)

            when {
                escaping -> {
                    argument.append(char)
                    escaping = false
                }
                char == '\\' -> escaping = true
                quoting && char == '"' -> quoting = false // accept other quote chars
                !quoting && char == '"' -> quoting = true // accept other quote chars
                !quoting && char == delimiter -> {
                    // Maybe this should throw? !test  blah -- Extraneous whitespace is ignored.
                    if (argument.isEmpty()) continue@loop
                    else break@loop
                }
                else -> argument.append(char)
            }
        }

        argument.append('"')

        val remainingArgs = StringBuilder().apply {
            iterator.forEachRemaining { this.append(it) }
        }
        args = remainingArgs.toString().split(delimiter).toMutableList()
        return Pair(argument.toString(), original.split(delimiterStr))
    }

    /**
     * @returns a Pair of the parsed argument, and the original args.
     */
    private fun getNextArgument(greedy: Boolean): Pair<String, List<String>> {
        val (argument, original) = when {
            args.isEmpty() -> Pair("", emptyList())
            greedy -> {
                val args = take(args.size)
                Pair(args.joinToString(delimiterStr), args)
            }
            args[0].startsWith('"') && delimiter == ' ' -> parseQuoted() // accept other quote chars
            else -> {
                val taken = take(1)
                Pair(taken.joinToString(delimiterStr), taken)
            }
        }

        var unquoted = argument.trim()

        if (!greedy) {
            unquoted = unquoted.removeSurrounding("\"")
        }

        return Pair(unquoted, original)
    }

    fun parse(arg: ArgumentEntity): Any? {
        val parser = parsers[arg.type]?: throw ParserNotRegistered("No parsers registered for `${arg.type}`")
        val (argument, original) = getNextArgument(arg.greedy)
        val result = if (argument.isEmpty()) {
            Optional.empty()
        } else {
            try {
                parser.parse(ctx, argument)
            } catch (e: Exception) {
                throw BadArgument(arg, argument, e)
            }
        }

        val canSubstitute = arg.isTentative || arg.isNullable || (arg.optional && argument.isEmpty())

        if (!result.isPresent && !canSubstitute) { // canSubstitute -> Whether we can pass null or the default value.
            // This should throw if the result is not present, and one of the following is not true:
            // - The arg is marked tentative (isTentative)
            // - The arg can use null (isNullable)
            // - The arg has a default (isOptional) and no value was specified for it (argument.isEmpty())

            //!arg.isNullable && (!arg.optional || argument.isNotEmpty())) {
            throw BadArgument(arg, argument)
        }

        if (!result.isPresent && arg.isTentative) {
            restore(original)
        }

        return result.orElse(null)
    }

    companion object {
        val parsers = hashMapOf<Class<*>, Parser<*>>()

        val init = fun () {
            // Kotlin types and primitives
            val booleanParser = BooleanParser()
            this.parsers[Boolean::class.java] = booleanParser
            this.parsers[java.lang.Boolean::class.java] = booleanParser

            val doubleParser = DoubleParser()
            this.parsers[Double::class.java] = doubleParser
            this.parsers[java.lang.Double::class.java] = doubleParser

            val floatParser = FloatParser()
            this.parsers[Float::class.java] = floatParser
            this.parsers[java.lang.Float::class.java] = floatParser

            val intParser = IntParser()
            this.parsers[Int::class.java] = intParser
            this.parsers[java.lang.Integer::class.java] = intParser

            val longParser = LongParser()
            this.parsers[Long::class.java] = longParser
            this.parsers[java.lang.Long::class.java] = longParser

            // JDA entities
            val inviteParser = InviteParser()
            this.parsers[Invite::class.java] = inviteParser
            this.parsers[net.dv8tion.jda.api.entities.Invite::class.java] = inviteParser

            this.parsers[Member::class.java] = MemberParser()
            this.parsers[Role::class.java] = RoleParser()
            this.parsers[TextChannel::class.java] = TextChannelParser()
            this.parsers[User::class.java] = UserParser()
            this.parsers[VoiceChannel::class.java] = VoiceChannelParser()

            // Custom entities
            this.parsers[Emoji::class.java] = EmojiParser()
            this.parsers[String::class.java] = StringParser()
            this.parsers[Snowflake::class.java] = SnowflakeParser()
            this.parsers[URL::class.java] = UrlParser()
        }

        fun parseArguments(cmd: Executable, ctx: Context, args: List<String>, delimiter: Char): HashMap<KParameter, Any?> {
            if (cmd.arguments.isEmpty()) {
                return hashMapOf()
            }

            val commandArgs = if (delimiter == ' ') args else args.joinToString(" ").split(delimiter).toMutableList()
            val parser = ArgParser(ctx, delimiter, commandArgs)
            val resolvedArgs = hashMapOf<KParameter, Any?>()

            for (arg in cmd.arguments) {
                val res = parser.parse(arg)
                val useValue = res != null || (arg.isNullable && !arg.optional) || (arg.isTentative && arg.isNullable)

                if (useValue) {
                    //This will only place the argument into the map if the value is null,
                    // or if the parameter requires a value (i.e. marked nullable).
                    //Commands marked optional already have a parameter so they don't need user-provided values
                    // unless the argument was successfully resolved for that parameter.
                    resolvedArgs[arg.kparam] = res
                }
            }

            return resolvedArgs
        }
    }
}
