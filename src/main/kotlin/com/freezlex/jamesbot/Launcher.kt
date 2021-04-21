package com.freezlex.jamesbot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Launcher(@Autowired var bot: Bot): ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        this.bot.start()
    }
}
