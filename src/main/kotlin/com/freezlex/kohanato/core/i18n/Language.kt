package com.freezlex.kohanato.core.i18n

import com.freezlex.kohanato.core.logger
import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

object Language: HashMap<String, LanguageModel>() {

    fun loadLanguage(pckg: String){
        val f = File(pckg)
        for(pathname in  f.list()){
            addLang(Files.readString(Path.of("${f.absolutePath}/$pathname"), StandardCharsets.UTF_8))
        }
        logger.info("${this.size} languages have been registered.")
    }

    private fun addLang(data: String){
        val model = Gson().fromJson(data, LanguageModel::class.java)
        if(model.regCode != "MODEL"){
            this[model.regCode] = model
            logger.debug("Translation file ${model.regCode} registered.")
        }
    }
}