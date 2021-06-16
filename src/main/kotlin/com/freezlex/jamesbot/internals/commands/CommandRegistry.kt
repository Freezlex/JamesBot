package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.indexer.Indexer
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.lang.RuntimeException

class CommandRegistry: HashMap<String, CommandFunction>() {

    fun getCommands(): HashMap<String, CommandFunction>{
        return this;
    }

    fun findCommandByName(name: String): CommandFunction? {
        return this[name]
    }

    fun findCommandByAlias(alias: String): CommandFunction?{
        return this.values.firstOrNull { it.properties.aliases()?.contains(alias) ?: false }
    }

    fun register(packageName: String): CommandRegistry{
        logger.debug("Running through $packageName package for commands")
        val idx = Indexer(packageName)
        for(cmd in idx.getCmds()){
            val command = idx.loadCommand(idx.getCommand(cmd), cmd);
            if(this.containsKey(command.name.lowercase()))throw RuntimeException("Cannot register command ${command.name}; the trigger has already been registered.")
            this[command.name.lowercase()] = command
        }
        logger.debug("End registering package found ${this.size} commands")
        // Cascade method call
        return this;
    }

    fun createSlash(jda: JDA, clearExistent: Boolean): CommandRegistry{
        if(this.isEmpty())return this
        val slashList: MutableList<CommandData> = mutableListOf()
        this.getCommands().forEach {
            if(it.value.properties.isSlash()){
                slashList.add(
                    CommandData(it.value.name, it.value.properties.description()!!)
                        .addOptions(it.value.arguments.map{ arg -> OptionData(arg.slashType, arg.name, arg.description, !arg.isNullable)
                            .addChoices(arg.options.map { opt -> Command.Choice(opt, opt) }) }))
            }
        }
        if(clearExistent)jda.updateCommands().addCommands(slashList).queue() else slashList.forEach { jda.upsertCommand(it).queue() }

        return this
    }
}
