package com.freezlex.jamesbot.internals.i18n

import com.freezlex.jamesbot.internals.i18n.category.CategoryLang

data class LanguageModel(
    val common: CommonLang,
    val category: CategoryLang,
    val exception: ExceptionsLang,
    val code: String
)
