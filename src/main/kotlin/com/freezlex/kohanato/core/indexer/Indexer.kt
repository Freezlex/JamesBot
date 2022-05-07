package com.freezlex.kohanato.core.indexer

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.commands.arguments.Argument
import com.freezlex.kohanato.core.commands.arguments.Param
import com.freezlex.kohanato.core.commands.contextual.BaseCommand
import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.commands.parser.InferType
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import org.reflections.Reflections
import org.reflections.scanners.MethodParameterNamesScanner
import org.reflections.scanners.Scanners
import java.io.File
import java.lang.reflect.Modifier
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

class Indexer {
    private val jar: Jar?
    private val pckgName: String
    private val reflections: Reflections
    private val classLoader: URLClassLoader?

    constructor(pckgName: String){
        this.pckgName = pckgName
        this.classLoader = null
        this.jar = null
        reflections = Reflections(pckgName, MethodParameterNamesScanner(), Scanners.SubTypes)
    }

    constructor(pckgName: String, jarPath: String){
        this.pckgName = pckgName
        val commandJar = File(jarPath)
        check(commandJar.exists()) { "jarPath points to a non-existent file. jarPath:${jarPath}" }
        check(commandJar.extension == "jar") { "jarPath leads to a file which is not a jar. jarPath:${jarPath}" }
        val path = URL("jar:file:${commandJar.absolutePath}!/")
        this.classLoader = URLClassLoader.newInstance(arrayOf(path))
        this.jar = Jar(commandJar.nameWithoutExtension, commandJar.absolutePath, pckgName, classLoader)
        reflections = Reflections(pckgName, this.classLoader, MethodParameterNamesScanner(), Scanners.SubTypes)
    }

    fun getCommands(): List<BaseCommand>{
        val command = reflections.getSubTypesOf(BaseCommand::class.java)
        return command.filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface && BaseCommand::class.java.isAssignableFrom(it) }
            .map { it.getDeclaredConstructor().newInstance() }
    }

    fun getCommand(command: BaseCommand): KFunction<*>{
        val itClass = command::class
        val runner = itClass.members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == "run"}
        return runner!!
    }

    fun loadCommand(method: KFunction<*>, command: BaseCommand): KoCommand {
        require(method.javaMethod!!.declaringClass == command::class.java){ "${method.name} is not from ${command::class.simpleName}" }

        val name = command.name?.lowercase()?: throw IllegalStateException("The command ${command::class.java.name} is missing a name.")
        val category = command.category
        val cooldown = command.cooldown
        // TODO : Refactor this check but we need to find a global scope to check instead of pointing class
        val event = method.valueParameters.firstOrNull() {
            it.type.classifier?.equals(SlashCommandInteractionEvent::class) == true || it.type.classifier?.equals(UserContextInteractionEvent::class) == true || it.type.classifier?.equals(MessageContextInteractionEvent::class) == true
        }
        require(event != null) { "${method.name} method from $name command is missing the event parameters!" }
        val context = method.valueParameters.firstOrNull() {
            it.type.classifier?.equals(KoListener::class) == true
        }
        require(context != null) { "${method.name} method from $name command is missing the KoListener parameters!" }
        val arguments = loadParameters(method.valueParameters.filterNot {
            it.type.classifier?.equals(SlashCommandInteractionEvent::class) == true || it.type.classifier?.equals(UserContextInteractionEvent::class) == true || it.type.classifier?.equals(MessageContextInteractionEvent::class) == true || it.type.classifier?.equals(KoListener::class) == true
        })

        return KoCommand(name, category, this.jar, cooldown, method, command, arguments, context, event)
    }

    private fun loadParameters(parameters: List<KParameter>): List<Argument>{
        val arguments = mutableListOf<Argument>()

        parameters.forEach {
            val p = it.findAnnotation<Param>()
            val name = if(p?.name == "" || p?.name == null) it.name.toString() else p.name
            val type = it.type.jvmErasure.javaObjectType
            val greedy = p?.greedy?: false
            val inferredType = InferType.predicate(it.type)
            val optional = it.isOptional
            val option = p?.options?: arrayOf()
            val nullable= it.type.isMarkedNullable
            val description = p?.description?: "No description provided"
            val tentative = if(p?.tentative == true && !(nullable || optional)) {
                throw IllegalStateException("${p.name} is marked as tentative, but does not have a default value and is not marked nullable!")
            }else{
                p?.tentative?: false
            }
            arguments.add(Argument(name, type, greedy, optional, nullable, tentative, inferredType, description, option.toMutableList(), it))
        }
        return arguments
    }
}
