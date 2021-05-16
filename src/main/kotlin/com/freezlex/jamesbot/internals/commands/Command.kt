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
    private val arguments: MutableList<Argument>?,
    val description : String?,
    private val ownerOnly : Boolean,
    private val userPermission : List<Permission>?,
    private val botPermission : List<Permission>?,
    private val hidden : Boolean = false
){
    /**
     * Runner for the command
     * @throws Exception If not implemented the command will throw an error
     */
    open fun run(message: MessageModel, repositoryManager: RepositoryManager){
        throw Exception("Commands must implement run method")
    }

    /**
     * Execute all the check required and all the verification before trying to run the command
     * @param messageModel The incoming message received
     * @param repositoryManager The repository manager
     */
    fun execute(messageModel: MessageModel, repositoryManager: RepositoryManager): MessageModel{
        var message = messageModel
        if(ownerOnly && message.event.author.id != "306703362261254154") throw Exception("Azi zebi t'es pas l'owner qu'est ce que tu viens essayer de faire cette commande")
        message.validateQueue.remove("") // removing all empty args
        //Checking the user permissions
        if(this.userPermission != null){
            if(message.event.member?.permissions?.containsAll(this.userPermission) != true) throw Exception("Sorry but you are missing some discord permission to perform this action. Required permissions : `${this.userPermission.joinToString("`, `")}`")
            if(this.botPermission != null && this.botPermission.let { message.event.guild.selfMember.hasPermission(message.event.channel, it) }) throw Exception("Sorry but I'm missing some discord permission to perform this action. Required permissions : `${this.botPermission?.joinToString("`, `")}`")
        }

        // Validating all the args
        if (this.arguments != null) {
            for(argument in this.arguments){ // Loop to validate all the args passed in the message
                if(message.validateQueue.size == 0){
                    if(argument.default != null){
                        message.validateQueue.add(argument.default.toString())
                    }else{
                        throw Exception("Missing argument ${argument.name}")
                    }
                }
                message = argument.execute(message) // Validating all the args
                message.validateQueue.removeAt(0) // Remove the args from the list for the rest of the checkin
            }
        }

        this.run(message, repositoryManager)
        return message;
    }


}
