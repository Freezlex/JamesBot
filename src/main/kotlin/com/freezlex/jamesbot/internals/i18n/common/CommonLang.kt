package com.freezlex.jamesbot.internals.i18n.common

import com.google.gson.annotations.SerializedName

data class CommonLang (
    val prefix: String,
    @SerializedName(value = "lang_code")
    val langCde: HashMap<String, String>)
