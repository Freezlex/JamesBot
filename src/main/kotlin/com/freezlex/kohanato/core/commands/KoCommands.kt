package com.freezlex.kohanato.core.commands

import com.freezlex.kohanato.core.commands.contextual.KoCommand
import com.freezlex.kohanato.core.commands.contextual.MessageContextCommand
import com.freezlex.kohanato.core.commands.contextual.SlashCommand
import com.freezlex.kohanato.core.commands.contextual.UserContextCommand
import com.freezlex.kohanato.core.indexer.Indexer
import com.freezlex.kohanato.core.logger
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.*

object KoCommands: HashMap<String, KoCommand>() {

    /** Get all the commands from the command collection */
    private fun getCommands(): HashMap<String, KoCommand>{
        return this;
    }

    /** Get all the commands from the command collection which are from the type SlashCommand */
    fun getSlashCommands(): Map<String, KoCommand>{
        return this.filter { it.value.command is SlashCommand}
    }

    fun getMessageContextCommands(): Map<String, KoCommand>{
        return this.filter { it.value.command is MessageContextCommand }
    }

    fun getUserContextCommands(): Map<String, KoCommand>{
        return this.filter { it.value.command is UserContextCommand }
    }

    /**
     * Find a command by his name
     * @param name
     */
    fun findCommandByName(name: String): KoCommand?{
        return this[name];
    }

    /**
     * Find a command by his alias
     * @param alias The alias of the command
     */
    fun findCommandByAlias(alias: String): KoCommand? {
        return this.values.firstOrNull() { true };
    }

    fun register(packageName: String): KoCommands{
        logger.debug("Running through $packageName package for commands")
        val idx = Indexer(packageName)
        val duplicate = HashMap<String, MutableList<KoCommand>>()

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
                command.name = "${command.category.short}-${command.name}"
                if(this.getCommands().containsKey(command.name))throw RuntimeException("Cannot register command ${command.name} from the ${command.category.fName} category; The unique trigger has already been registered.")
                command.command.aliases?.forEachIndexed { i, it -> command.command.aliases!![i] = "${command.category.short + "-"}${it}" }
                this[command.name.lowercase()] = command
            }
        }

        logger.debug("End registering package, found ${this.size} commands")
        // Cascade method call
        return this;
    }

    fun syncCommands(jda: JDA, clearExistent: Boolean): KoCommands {

        val slash = this.getSlashCommands()
        if(slash.isEmpty()) logger.warn("Cannot find any slash command at the moment to be registered on the API. Please make sure, you've registered some before syncing commands with the API !")
        val userContext = this.getUserContextCommands()
        if(userContext.isEmpty()) logger.warn("Cannot find any user command at the moment to be registered on the API. Please make sure, you've registered some before syncing commands with the API !")
        if(userContext.size > 5) { logger.error("Could not register more than 5 user context commands."); return this }
        val messageContext = this.getMessageContextCommands()
        if(messageContext.isEmpty()) logger.warn("Cannot find any message command at the moment to be registered on the API. Please make sure, you've registered some before syncing commands with the API !")
        if(messageContext.size > 5) { logger.error("Could not register more than 5 user message commands."); return this }

        logger.debug("Syncing slash commands on the Discord API ...")
        val groupedSlash = HashMap<Categories, MutableList<KoCommand>>()
        val slashList: MutableList<CommandData> = mutableListOf();

        slash.filter { it.value.command.isSubGroup && it.value.category.grouped }.forEach {
            val temp = groupedSlash.getOrDefault(it.value.category, mutableListOf())
            temp.add(it.value)
            groupedSlash[it.value.category] = temp
        }

        groupedSlash.forEach { slashList.add(createGroupedSlash(it.key, it.value)) }
        slash.filterNot { it.value.command.isSubGroup && it.value.category.grouped }.let { createNonGroupedSlash(it.values.toMutableList()).forEach { s -> slashList.add(s) } }

        jda.updateCommands()
            .addCommands(slashList)
            .addCommands(userContext.map { Commands.user(it.value.name)})
            .addCommands(messageContext.map { Commands.message(it.value.name)})
            .queue()
        return this;
    }

    private fun createGroupedSlash(ca: Categories, co: MutableList<KoCommand>): SlashCommandData{
        return Commands.slash(ca.fName, ca.description)
            .addSubcommands(co.map { SubcommandData(it.name, it.command.description)
                .addOptions(it.arguments.sortedBy { a -> a.optional }.map {a -> OptionData(a.inferredType, a.name, a.description, !a.optional)
                    .addChoices(a.options.map { o -> Command.Choice(o , o) }) }) })
    }

    private fun createNonGroupedSlash(c: MutableList<KoCommand>): List<SlashCommandData>{
        return c.map {
            Commands.slash(it.name, it.command.description).addOptions(
                it.arguments.sortedByDescending { a -> a.optional }.map {a -> OptionData(a.inferredType, a.name, a.description, a.optional)
                    .addChoices(a.options.map { o -> Command.Choice(o , o) }) })
        }
    }
}
