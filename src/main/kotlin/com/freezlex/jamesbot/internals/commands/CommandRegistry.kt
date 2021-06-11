package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.indexer.Indexer
import com.freezlex.jamesbot.logger
import java.lang.RuntimeException

class CommandRegistry: HashMap<String, CommandFunction>() {

    fun findCommandByName(name: String): CommandFunction? {
        return this[name]
    }

    fun findCommandByAlias(alias: String): CommandFunction?{
        return this.values.firstOrNull { it.properties.aliases()?.contains(alias) ?: false }
    }

    fun register(packageName: String){
        logger.debug("Running through $packageName package for commands")
        val idx = Indexer(packageName)
        for(cmd in idx.getCmds()){
            val command = idx.loadCommand(idx.getCommand(cmd), cmd);
            if(this.containsKey(command.name))throw RuntimeException("Cannot register command ${command.name}; the trigger has already been registered.")
            this[command.name] = command
        }
        logger.debug("End registering package found ${this.size} commands")
    }
}
