package com.freezlex.jamesbot.database.models

import org.hibernate.cfg.Configuration
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase
import org.hibernate.testing.transaction.TransactionUtil
import org.junit.Test
import java.io.IOException
import java.util.*
import kotlin.test.assertEquals

class HibernateModelsTesting: BaseCoreFunctionalTestCase() {
    private val properties: Properties
        @Throws(IOException::class)
        get() {
            val properties = Properties()
            properties.load(javaClass.classLoader.getResourceAsStream("hibernate.properties"))
            println(properties)
            return properties
        }

    override fun getAnnotatedClasses(): Array<Class<*>> {
        return arrayOf(UsersModel::class.java)
    }

    override fun configure(configuration: Configuration) {
        super.configure(configuration)
        configuration.properties = properties
    }

    @Test
    fun registerUserWithFullData_whenSaved_thenFound(){
        TransactionUtil.doInHibernate<Unit>(({this.sessionFactory()}), { session ->
            val userToSave = UsersModel(306703362261254154)
            session.persist(userToSave);
            val userFound = session.find(UsersModel::class.java, userToSave.userId)

            assertEquals(userToSave, userFound)
        })
    }
}
