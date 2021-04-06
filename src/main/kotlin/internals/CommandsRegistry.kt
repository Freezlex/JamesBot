package com.freezlex.jamesbot.internals

import com.freezlex.jamesbot.commands.Command
import org.reflections.Reflections

class CommandsRegistry {

    fun loadCommands() {
        val reflections: Set<Class<out Command?>> = Reflections("${this.javaClass.packageName.removeSuffix(".internals")}.commands.implementation").getSubTypesOf(Command::class.java)
        for (commandClass in reflections) commands.add(commandClass.getConstructor().newInstance()!!)
    }

    companion object{
        private val commands = mutableListOf<Command>();

        fun registerCommand(cmd: Command): Boolean = commands.add(cmd);

        fun getCommandByName(name: String): Command? = commands.find { it.name == name || name in it.alias}

        fun getCommands(): MutableList<Command> = commands;
    }
}
