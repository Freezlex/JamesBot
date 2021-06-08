package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.api.Indexer
import java.lang.RuntimeException

class CommandRegistry: HashMap<String, CommandFunction>() {

    fun findCommandByName(name: String): CommandFunction? {
        return this[name]
    }

    fun findCommandByAlias(alias: String): CommandFunction?{
        return this.values.firstOrNull { it.cmd.aliases()?.contains(alias) ?: false }
    }

    fun register(packageName: String){
        val idx = Indexer(packageName)
        for(cmd in idx.getCmds()){
            val command = idx.loadCommand(idx.getCommand(cmd), cmd);
            if(this.containsKey(command.name))throw RuntimeException("Cannot register command ${command.name}; the trigger has already been registered.")
            this[command.name] = command
        }
    }
}
