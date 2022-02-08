package com.freezlex.kohanato.core.commands

import com.freezlex.kohanato.core.commands.contextual.Command
import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import com.freezlex.kohanato.core.indexer.Indexer
import com.freezlex.kohanato.core.logger

object Commands: HashMap<String, Command>() {

    /** Get all the commands from the command collection */
    fun getCommands(): HashMap<String, Command>{
        return this;
    }

    /** Get all the commands from the command collection which are from the type SlashCommand */
    fun getSlashCommands(): Map<String, Command>{
        return this.filter { it.value.command.javaClass == SlashCommand::javaClass }
    }

    /**
     * Find a command by his name
     * @param name
     */
    fun findCommandByName(name: String): Command?{
        return this[name];
    }

    /**
     * Find a command by his alias
     * @param alias The alias of the command
     */
    fun findCommandByAlias(alias: String): Command? {
        return this.values.firstOrNull() { true };
    }

    fun register(packageName: String): Commands{
        logger.debug("Running through $packageName package for commands")
        val idx = Indexer(packageName)
        val duplicate = HashMap<String, MutableList<Command>>()

        // Command iterator to find any duplicated command
        for(cmd in idx.getCommands()){
            val command = idx.loadCommand(idx.getCommand(cmd), cmd);
            when{
                duplicate.containsKey(command.name.lowercase())->duplicate[command.name.lowercase()]!!.add(command)
                this.containsKey(command.name.lowercase()) -> {
                    val temp = duplicate.getOrDefault(command.name.lowercase(), mutableListOf())
                    temp.add(command)
                    temp.add(this[command.name.lowercase()]!!)
                    this.remove(command.name.lowercase())
                    duplicate[command.name.lowercase()] = temp
                }
                else -> this[command.name.lowercase()] = command
            }
        }

        // Logical duplicated entry iterator to replace them into the command registry
        for(d in duplicate.values){
            d.forEach { command ->
                command.name = "${command.category}-${command.name}"
                if(this.getCommands().containsKey(command.name))throw RuntimeException("Cannot register command ${command.name} from the ${command.category} category; The unique trigger has already been registered.")
                command.command.aliases?.forEachIndexed { i, it -> command.command.aliases!![i] = "${command.category + "-"}${it}" }
                this[command.name.lowercase()] = command
            }
        }

        logger.debug("End registering package, found ${this.size} commands")
        // Cascade method call
        return this;
    }

}