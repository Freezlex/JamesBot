package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.lang.Exception

/**
 * Constructor for a command
 */
abstract class Command(
    val name : String,
    val alias: List<String>?,
    private val arguments: List<Argument>?,
    val description : String?,
    val ownerOnly : Boolean,
    val userPermission : List<Permission>?,
    val botPermission : List<Permission>?
){
    /**
     * Runner for the command
     * @return Any
     */
    open fun run(event: GuildMessageReceivedEvent, repositoryManager: RepositoryManager) : Boolean{
        return true;
    }

    fun execute(event: GuildMessageReceivedEvent, repositoryManager: RepositoryManager, parser: MatchResult): MessageModel{
        // TODO : Create parser
        val parsedString = event.message.contentRaw.removePrefix(parser.groupValues[0])
        var message = MessageModel(parser, event, parsedString, parsedString.split(" ").toMutableList(), listOf())
        message.validateQueue.remove("")
        if (this.arguments != null) {
            try{
                for(argument in this.arguments){ // Loop to validate all the args passed in the message
                    message = argument.execute(message)
                }
            }catch (e: Exception){
                e.message?.let { event.message.reply(it).mentionRepliedUser(false).queue() }
            }

        }else{
            println("Required args are null")
        }
        return message;
    }
}
