package com.example.kloth.ui.utils

sealed interface TranslationUiStatus {
    data object Original : TranslationUiStatus
    data object Translating : TranslationUiStatus
    data class Translated(val text: String) : TranslationUiStatus
    data object Error : TranslationUiStatus
}

data class TranslationTextState(
    val canTranslate: Boolean = false,
    val isDetectingLanguage: Boolean = false,
    val status: TranslationUiStatus = TranslationUiStatus.Original
)

object TranslationContentIds {
    fun description(articleId: String) = "description:$articleId"
    fun review(reviewId: String) = "review:$reviewId"
    fun comment(reviewId: String, commentId: String) = "comment:$reviewId:$commentId"
}
