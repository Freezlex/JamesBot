package com.freezlex.kohanato.core.indexer

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
