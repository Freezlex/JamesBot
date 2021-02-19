package utils

import utils.Bot
import utils.Settings

object Launcher {
    @JvmStatic
    fun main(args: Array<String>){
        val bot = Bot(Settings.BOT_TOKEN, listOf());

        bot.start();
    }
}
