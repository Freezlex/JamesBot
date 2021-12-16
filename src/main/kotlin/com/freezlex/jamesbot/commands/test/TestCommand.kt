package com.freezlex.jamesbot.commands.test

import com.freezlex.jamesbot.api.contextual.SlashCommand
import com.freezlex.jamesbot.api.contextual.TextualCommand

class TestSlashCommand: TextualCommand {
    override val category: String = "TEST"
}

class TestTextualCommand: SlashCommand {
    override val category: String = "TEST"
}