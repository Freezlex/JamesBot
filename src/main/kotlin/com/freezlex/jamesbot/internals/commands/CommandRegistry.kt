package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.internals.indexer.Indexer
import com.freezlex.jamesbot.logger
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import java.lang.RuntimeException

object CommandRegistry: HashMap<String, CommandFunction>() {

    /**
     * Get all the commands for the command registry
     */
    fun getCommands(): HashMap<String, CommandFunction>{
        return this;
    }

    /**
     * Find a command by his name
     * @param name
     */
    fun findCommandByName(name: String): CommandFunction? {
        return this[name]
    }

    /**
     * Find a command by his alias
     * @param alias The alias of the command
     */
    fun findCommandByAlias(alias: String): CommandFunction?{
        return this.values.firstOrNull { it.properties.aliases()?.contains(alias) ?: false }
    }

    /**
     * Register a package as command Registry
     * @param packageName The location of the package where are located all the commands
     */
    fun register(packageName: String): CommandRegistry{
        logger.debug("Running through $packageName package for commands")
        val idx = Indexer(packageName)
        val duplicate = HashMap<String, MutableList<CommandFunction>>()

        // Command iterator to find any duplicated command
        for(cmd in idx.getCmds()){
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
            d.forEach { cmd ->
                cmd.name = if(cmd.category.short != null) "${cmd.category.short}-${cmd.name}" else cmd.name
                if(this.getCommands().containsKey(cmd.name))throw RuntimeException("Cannot register command ${cmd.name} from the ${cmd.category.category} category; The unique trigger has already been registered.")
                cmd.cmd.aliases()?.forEachIndexed { i, it -> cmd.cmd.aliases()!![i] = "${cmd.category.short + "-"}${it}" }
                this[cmd.name.lowercase()] = cmd
            }
        }

        logger.debug("End registering package, found ${this.size} commands")
        // Cascade method call
        return this;
    }

    /**
     * Init all the slash commands from the commandRegistry
     * @param jda The JDA client
     * @param clearExistent Clear all the existing commands in the discord API ?
     */
    fun createSlash(jda: JDA, clearExistent: Boolean): CommandRegistry{
        if(this.isEmpty())return this
        logger.debug("Converting all provided commands to slash commands")
        val slashGroups = HashMap<CommandCategory, MutableList<CommandFunction>>()
        val slashList: MutableList<CommandData> = mutableListOf()

        // Indexing all the commands by groups
        this.getCommands().forEach {
            if(it.value.cmd.isSlash()){
                val temp = slashGroups.getOrDefault(it.value.cmd.category(), mutableListOf())
                temp.add(it.value)
                slashGroups[it.value.cmd.category()] = temp
            }
        }

        // Creating all the commands data and subcommands from the slashGroups
        slashGroups.forEach {
            if(it.key.regrouped && it.value.size > 0) slashList.add(createGroupedSlashCommands(it.key, it.value))
            else if(!it.key.regrouped && it.value.size > 0) createNonGroupedSlashCommands(it.value).forEach {slash -> slashList.add(slash) }
        }
        if(clearExistent)jda.updateCommands().addCommands(slashList).queue() else slashList.forEach { jda.upsertCommand(it).queue() }
        logger.info("${slashList.size} commands converted to slash commands.")
        return this
    }

    // Create a group of command slash commands
    private fun createGroupedSlashCommands(category: CommandCategory,commands: MutableList<CommandFunction>): CommandData{
        val tempCmd = CommandData(category.category, category.description)
        commands.map { cmd ->
            val tempSub = SubcommandData(cmd.cmd.name()!!.lowercase(), cmd.cmd.description())
            if(cmd.arguments.size > 0) tempSub.addOptions(cmd.arguments.filter{ arg -> arg.options.size > 0}.map {arg -> OptionData(arg.slashType, arg.name.lowercase(), arg.description, !arg.isNullable)
                .addChoices(arg.options.map { opt -> Command.Choice(opt.lowercase(), opt) })})
                .addOptions(cmd.arguments.filter { arg -> arg.options.size == 0} .map { arg -> OptionData(arg.slashType, arg.name.lowercase(), arg.description, !arg.isNullable) })
            tempCmd.addSubcommands(tempSub)
        }
        return tempCmd
    }

    // Create individual slash commands
    private fun createNonGroupedSlashCommands(commands: MutableList<CommandFunction>): MutableList<CommandData>{
        val tempCmds = mutableListOf<CommandData>()
        commands.forEach {
            val tempCmd = CommandData(it.cmd.name()!!.lowercase(), it.properties.description())
            if(it.arguments.isNotEmpty()) tempCmd.addOptions(it.arguments.filter{ arg -> arg.options.size > 0}.map { arg -> OptionData(arg.slashType, arg.name.lowercase(), arg.description, !arg.isNullable)
                .addChoices(arg.options.map { opt -> Command.Choice(opt.lowercase(), opt) })})
                .addOptions(it.arguments.filter { arg -> arg.options.size == 0} .map { arg -> OptionData(arg.slashType, arg.name.lowercase(), arg.description, !arg.isNullable) })
            tempCmds.add(tempCmd)
        }
        return tempCmds;
    }
}
