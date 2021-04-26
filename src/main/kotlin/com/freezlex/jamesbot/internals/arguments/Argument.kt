package com.freezlex.jamesbot.internals.arguments

/**
 * Constructor for an argument
 * TODO : Same principe as the command registry
 */
abstract class Argument{

    fun validate(args: String, msg: String){
        throw Error("Argument must have a run method")
    }

    /**
     * Runner for the custom argument
     */
    fun run(){
        throw Error("Argument must have a run method")
    }
}
