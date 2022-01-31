package com.freezlex.kohanato.api.indexer

import com.freezlex.kohanato.api.contextual.BaseCommand
import org.reflections.Reflections
import org.reflections.scanners.MethodParameterNamesScanner
import org.reflections.scanners.Scanners
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.KFunction

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

    fun loadCommand(method: KFunction<*>, command: BaseCommand):
}
