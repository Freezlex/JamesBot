package com.freezlex.jamesbot

import com.freezlex.jamesbot.database.entity.GuildEntity
import com.freezlex.jamesbot.database.entity.UserEntity
import com.freezlex.jamesbot.database.repository.GuildRepository
import com.freezlex.jamesbot.database.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JamesbotApplicationTests @Autowired constructor(
	val entityManager: TestEntityManager,
	val userRepository: UserRepository,
	val guildRepository: GuildRepository
) {
	@Test
	fun `When findUserByIdOrNull then return UserModel`(){
		val user = entityManager.persist(UserEntity(0, 123456789012345678, "User", "9999"))
		entityManager.flush()
		val userFound = userRepository.findByIdOrNull(user.id)
		assertEquals(user, userFound)
	}

	@Test
	fun `When findGuildByIdOrNull then return GuildModel`(){
		val user = entityManager.persist(UserEntity(0, 123456789012345678, "User", "9999"))
		val guild = entityManager.persistAndFlush(GuildEntity(0, 306703362261254154, user))
		entityManager.flush()
		val guildFound = guildRepository.findByIdOrNull(guild.id)

		assertEquals(guild, guildFound)
	}

	@Test
	fun `When findSettingsByGuildOrNull then return GuildsSettings`(){

	}
}
