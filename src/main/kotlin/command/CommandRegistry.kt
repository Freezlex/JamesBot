package command

object CommandRegistry {
    private val commands: MutableMap<String, Command> = hashMapOf();
    private val aliases: MutableMap<String, Command> = hashMapOf();

    fun registerCommands(){

    }
}
