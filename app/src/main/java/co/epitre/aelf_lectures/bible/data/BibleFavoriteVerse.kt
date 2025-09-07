package co.epitre.aelf_lectures.bible.data

import java.io.Serializable

data class BibleFavoriteVerse(
    val bookRef: String,
    val chapterRef: String,
    val verseRef: String,
    val text: String
) : Serializable