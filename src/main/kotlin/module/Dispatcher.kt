package module

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Dispatcher {

    fun handleMessage(event: GuildMessageReceivedEvent){
        if(event.author.isBot)return;

    }

    private fun parseMessage(event: GuildMessageReceivedEvent){

    }
}
