package com.freezlex.jamesbot.internals.models

import com.freezlex.jamesbot.database.entity.UserEntity
import com.freezlex.jamesbot.database.entity.UserSettingsEntity
import com.freezlex.jamesbot.database.repository.GuildRepository
import com.freezlex.jamesbot.database.repository.GuildSettingsRepository
import com.freezlex.jamesbot.database.repository.UserRepository
import com.freezlex.jamesbot.database.repository.UserSettingsRepository
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserCache (
    var userRepository: UserRepository,
    var userSettingsRepository: UserSettingsRepository){
    fun initCache(event: GuildMessageReceivedEvent): UserSettingsEntity?{
        val setting = cache.find { s -> s.user.userId == event.author.idLong }
        if(setting == null){
            var user = userRepository.findOneByUserId(event.author.idLong).orElse(null)
            if(user == null){
                user = userRepository.save(UserEntity(event.author.idLong, event.author.name, event.author.asTag.split('#')[1]))
            }
            userSettingsRepository.save(UserSettingsEntity("EN", user)).let { cache.add(it) }
        }
        return cache.find { s -> s.user.userId == event.author.idLong };
    }

    fun getCache(event: GuildMessageReceivedEvent): UserSettingsEntity? {
        return cache.find { s -> s.user.userId == event.author.idLong };
    }

    companion object {
        private val cache: MutableList<UserSettingsEntity> = mutableListOf();
    }
}
