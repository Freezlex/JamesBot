package com.freezlex.jamesbot.internals.i18n

import com.freezlex.jamesbot.internals.i18n.category.CategoryLang
import com.freezlex.jamesbot.internals.i18n.common.CommonLang

data class LanguageModel(
    val common: CommonLang,
    val category: CategoryLang,
    val exception: ExceptionsLang,
    val name: String,
    val code: String
)
