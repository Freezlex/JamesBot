package com.freezlex.kohanato.core.cache

import com.freezlex.kohanato.core.KoListener
import com.freezlex.kohanato.core.cache.extension.ICache
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.minutes

class CacheManager(private val core: KoListener) {

    private val cache = Cache.Builder().expireAfterWrite(10.minutes).build<Int, ICache>()

    init {

    }

    private fun refeshCache(){

    }

}