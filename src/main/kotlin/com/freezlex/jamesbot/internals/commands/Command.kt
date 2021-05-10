package com.freezlex.jamesbot.internals.commands

import com.freezlex.jamesbot.database.repository.RepositoryManager
import com.freezlex.jamesbot.internals.arguments.Argument
import com.freezlex.jamesbot.internals.models.ArgumentModel
import com.freezlex.jamesbot.internals.models.MessageModel
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

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
        val message = MessageModel(parser, event, parsedString, parsedString.split(" ").toMutableList(), listOf())
        message.argsList.remove("")
        if (this.arguments != null) {
            for(argument in this.arguments){ // Loop to validate all the args passed in the message
                if(message.argsList.size == 0){
                    println("Missing argument ${argument.type}")
                    break
                }
                if(argument.length != 0){
                    // TODO : If the argument have a defined size
                    println("Pas encore implémenté")
                }else{
                    if(argument.validate(message.argsList[0], argument)){ // If args is valid
                        message.argsList.removeAt(0) // Remove the arg from the list
                        println("Valid")
                    }else{ // If args isn't valid
                        // TODO : Prompt for the invalid args
                        println("Nope")
                        message.argsList.removeAt(0)
                    }
                }
            }

        }else{
            println("Required args are null")
        }
        return message;
    }
}
