package com.freezlex.jamesbot.internals.arguments

abstract class Test {

    companion object{
        private val lol: MutableList<String> = mutableListOf()

        fun getValues(): Array<String>{
            return lol.toTypedArray()
        }
    }
}
