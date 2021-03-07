package com.freezlex.jamesbot.commands

import org.reflections.Reflections

class CommandsRegistry {

    fun loadCommands() {
        Reflections("com.freezlex.jamesbot.commands.utility")
            .getSubTypesOf(Command::class.java)
            .forEach { it.getDeclaredConstructor().newInstance()}
    }

    companion object{
        private val commands = mutableSetOf<Command>();

        fun registerCommand(cmd: Command): Boolean = commands.add(cmd);

        fun getCommandByName(name: String): Command? = commands.find { name in it.aliases }

        fun getCommands(): Set<Command> = commands;
    }
}
