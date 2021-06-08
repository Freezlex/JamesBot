package com.freezlex.jamesbot.implementation.test

import com.freezlex.jamesbot.internals.api.Context
import com.freezlex.jamesbot.internals.commands.Cmd

class TestCommand: Cmd {

    override fun name() = "Test"

    fun run(ctx: Context, string: String){
        println("Test")
    }
}
