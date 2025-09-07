package co.epitre.aelf_lectures.bible.biblefavoritesfragment

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import co.epitre.aelf_lectures.bible.data.BibleFavoriteVerse
import co.epitre.aelf_lectures.compose.theme.colors
import co.epitre.aelf_lectures.compose.theme.spacing
import co.epitre.aelf_lectures.compose.utils.Space

//TODO REF NOT WORKING Phm 9b-10.12-17

// TODO ALWAYS REF like this : Lc 14,25-33
// TODO                 or  :  Lc 14,25

@Composable
fun BibleFavoriteVerseComponent(
    verse: BibleFavoriteVerse,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(horizontal = spacing.s100, vertical = spacing.s50)) {


        Text(
            "${verse.bookRef} ${verse.chapterRef}, ${verse.verseRef}",
            color = colors.textFavorite,
            fontWeight = FontWeight.SemiBold
        )

        Space(spacing.s50)

        Text(
            verse.text,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
private fun Preview() {
   // BibleFavoriteVerseComponent()
}