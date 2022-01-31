package com.freezlex.kohanato.api.indexer

import java.net.URLClassLoader

class Jar (
    val name: String,
    val location: String,
    val pckgName: String,
    private val classLoader: URLClassLoader
        ){
    internal fun close(){
        classLoader.close()
    }
}
