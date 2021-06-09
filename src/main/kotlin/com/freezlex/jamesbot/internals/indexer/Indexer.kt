package com.freezlex.jamesbot.internals.indexer

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.commands.Cmd
import com.freezlex.jamesbot.internals.commands.CommandFunction
import org.reflections.Reflections
import org.reflections.scanners.MethodParameterNamesScanner
import org.reflections.scanners.SubTypesScanner
import java.io.File
import java.lang.reflect.Modifier
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

class Indexer {
    private val jar: Jar?
    private val packageName: String
    private val reflections: Reflections
    private val classLoader: URLClassLoader?

    constructor(packageName: String) {
        this.packageName = packageName
        this.classLoader = null
        this.jar = null
        reflections = Reflections(packageName, MethodParameterNamesScanner(), SubTypesScanner())
    }

    constructor(packageName: String, jarPath: String) {
        this.packageName = packageName

        val commandJar = File(jarPath)
        check(commandJar.exists()) { "jarPath points to a non-existent file." }
        check(commandJar.extension == "jar") { "jarPath leads to a file which is not a jar." }

        val path = URL("jar:file:${commandJar.absolutePath}!/")
        this.classLoader = URLClassLoader.newInstance(arrayOf(path))
        this.jar = Jar(commandJar.nameWithoutExtension, commandJar.absolutePath, packageName, classLoader)
        reflections = Reflections(packageName, this.classLoader, MethodParameterNamesScanner(), SubTypesScanner())
    }

    fun getCmds(): List<Cmd>{
        val cmds = reflections.getSubTypesOf(Cmd::class.java)
        return cmds
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface && Cmd::class.java.isAssignableFrom(it) }
            .map { it.getDeclaredConstructor().newInstance() }
    }

    fun getCommand(cmd: Cmd): KFunction<*>{
        val itClass = cmd::class
        val command = itClass.members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == "run" }
        return command!!
    }

    /**
     * @param method The method to execute when we want to launch the command
     */
    fun loadCommand(method: KFunction<*>, cmd: Cmd): CommandFunction{
        require(method.javaMethod!!.declaringClass == cmd::class.java){ "${method.name} is not from ${cmd::class.simpleName}" }

        val name = cmd.name()?.lowercase() ?: cmd::class.java.`package`.name.split('.').last().replace('_', ' ').lowercase()
        val category = cmd.category()?: "No category"
        val cooldown = cmd.cooldown()
        val ctxParam = method.valueParameters.firstOrNull { it.type.classifier?.equals(Context::class) == true }
        require(ctxParam != null) { "${method.name} is missing the Context parameter!" }
        val arguments = loadParameters(method.valueParameters.filterNot { it.type.classifier?.equals(Context::class) == true })

        return CommandFunction(name, category, this.jar, cooldown, method, cmd, arguments, ctxParam);
    }

    private fun loadParameters(parameters: List<KParameter>): List<Argument> {
        val arguments = mutableListOf<Argument>()

        for (p in parameters) {
            /** TODO : Implement a Name as annotation [p.findAnnotation<Name>()?.name ?:] */
            val pName = p.name ?: p.index.toString()
            val type = p.type.jvmErasure.javaObjectType
            /** TODO : Implement a Greddy argument as annotation [p.hasAnnotation<Greedy>()]*/
            val isGreedy = false
            val isOptional = p.isOptional
            val isNullable = p.type.isMarkedNullable
            /** TODO : Implement a Tentative as annotation [p.hasAnnotation<Tentative>()]*/
            val isTentative = false

            if (isTentative && !(isNullable || isOptional)) {
                throw IllegalStateException("${p.name} is marked as tentative, but does not have a default value and is not marked nullable!")
            }

            arguments.add(Argument(pName, type, isGreedy, isOptional, isNullable, isTentative, p))
        }

        return arguments
    }
}
