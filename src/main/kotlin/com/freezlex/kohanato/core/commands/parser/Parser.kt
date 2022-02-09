package com.freezlex.kohanato.core.commands.parser

import com.freezlex.kohanato.core.commands.arguments.Argument
import com.freezlex.kohanato.core.commands.parser.parsers.*
import com.freezlex.kohanato.core.indexer.Executable
import com.freezlex.kohanato.core.throwable.BadArgument
import com.freezlex.kohanato.core.throwable.ParserNotRegistered
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import java.net.URL
import java.util.*
import kotlin.jvm.internal.Intrinsics
import kotlin.reflect.KParameter
import kotlin.time.Duration

/**
 * The parser for the args
 * @param ctx
 *          The context of the event
 * @param delimiter
 *          The args delimiter char
 * @param commandArgs
 *          The args from the command
 */
class Parser(
    private val event: SlashCommandInteractionEvent,
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

    /**
     * Provide the parser for the arguments
     * @param arg
     *          The argument entity from the argument message
     */
    fun parse(arg: Argument): Any? {
        val parser = parsers[arg.type]?: throw ParserNotRegistered("No parsers registered for `${arg.type}`")
        val (argument, original) = getNextArgument(arg.greedy)
        val result: Optional<out Any> = if (argument.isEmpty()) {
            Optional.empty()
        } else {
            try {
                parser.parse(event, argument)
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
        val parsers = hashMapOf<Class<*>, Parsed<*>>()

        // TODO : Convert this to automatic loading

        fun init() {
            println("Register all parsers")
            // Kotlin types and primitives
            val booleanParser = BooleanParser()
            this.parsers[Boolean::class.java] = booleanParser
            this.parsers[java.lang.Boolean::class.java] = booleanParser

            this.parsers[String::class.java] = StringParser()

            val doubleParser = DoubleParser()
            this.parsers[Double::class.java] = doubleParser
            this.parsers[java.lang.Double::class.java] = doubleParser

            val floatParser = FloatParser()
            this.parsers[Float::class.java] = floatParser
            this.parsers[java.lang.Float::class.java] = floatParser

            val durationParser = DurationParser()
            this.parsers[Duration::class.java] = durationParser
            this.parsers[java.time.Duration::class.java] = durationParser

            this.parsers[Member::class.java] = MemberParser()
        }

        /**
         * Parse the argument from the message argument provided
         * @param cmd
         *          The invoked command
         * @param ctx
         *          The context of the incoming event
         * @param args
         *          The provided args in the command
         * @param delimiter
         *          The char delimiter for the command
         *
         * TODO : Simplify the parser we are not using string args anymore so we should use it only when someone decide to use a varargs as parameters
         */
        fun parseArguments(command: Executable, event: SlashCommandInteractionEvent, args: List<String>, delimiter: Char): HashMap<KParameter, Any?> {
            if (command.arguments.isEmpty()) {
                return hashMapOf()
            }

            val commandArgs = if (delimiter == ' ') args else args.joinToString(" ").split(delimiter).toMutableList()
            val parser = Parser(event, delimiter, commandArgs)
            val resolvedArgs = hashMapOf<KParameter, Any?>()

            for (arg in command.arguments) {
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

        /**
         * Parse the argument from the message argument provided
         * @param cmd
         *          The invoked command
         * @param ctx
         *          The context of the incoming event
         * @param args
         *          The provided args in the command
         * @param delimiter
         *          The char delimiter for the command
         */
        fun parseArguments(cmd: Executable, event: SlashCommandInteractionEvent, args: List<OptionMapping>?): HashMap<KParameter, Any?> {
            if(args == null)return hashMapOf();
            return parseArguments(cmd, event , args.map { it.asString }, ' ')
        }
    }
}
