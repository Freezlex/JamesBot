package command

interface Command {
    val name: String;
    val aliases: Array<String>?
    val usage: String;
    val allowPrivate: Boolean;
    val permission: Permission;
    val ownerOnly: Boolean;
    
    //https://github1s.com/KyuBlade/kotlin-discord-bot/blob/master/src/main/kotlin/com/omega/discord/bot/BotManager.kt
    fun run(event: Any);
    }
}
