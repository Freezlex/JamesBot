package com.freezlex.jamesbot.database.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RepositoryManager{
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var guildRepository: GuildRepository
    @Autowired lateinit var guildSettingsRepository: GuildSettingsRepository
    @Autowired lateinit var guildPermissionsRepository: GuildPermissionsRepository
}
