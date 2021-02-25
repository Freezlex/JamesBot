package utils

object Launcher {
    @JvmStatic
    fun main(args: Array<String>){
        val bot = Bot(Settings.BOT_TOKEN, listOf());

        bot.start();
    }
}
