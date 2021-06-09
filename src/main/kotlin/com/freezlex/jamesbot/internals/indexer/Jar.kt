package com.freezlex.jamesbot.internals.indexer

import java.net.URLClassLoader

class Jar(
    val name: String,
    val location: String,
    val packageName: String,
    private val classLoader: URLClassLoader
) {

    internal fun close() {
        classLoader.close()
    }

}
