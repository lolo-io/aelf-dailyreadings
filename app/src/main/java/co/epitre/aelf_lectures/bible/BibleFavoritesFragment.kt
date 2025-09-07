package co.epitre.aelf_lectures.bible

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import co.epitre.aelf_lectures.LecturesActivity
import co.epitre.aelf_lectures.R
import co.epitre.aelf_lectures.bible.biblebookfragment.components.BibleVerseComponent
import co.epitre.aelf_lectures.bible.biblebookfragment.components.previewBibleVerse
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import co.epitre.aelf_lectures.bible.biblefavoritesfragment.BibleFavoriteVerseComponent
import co.epitre.aelf_lectures.bible.data.BibleFavoriteVerse
import co.epitre.aelf_lectures.settings.SettingsActivity
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson

class BibleFavoritesFragment : BibleFragment() {

    var favoriteList = mutableListOf<BibleFavoriteVerse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.title =
            "Mes versets favoris"


        val settings =
            PreferenceManager.getDefaultSharedPreferences(
                requireContext().applicationContext
            )


        val gson = Gson()
        val json = settings.getString(SettingsActivity.KEY_FAVORITE_VERSES, "[]")
        val type = object : TypeToken<List<BibleFavoriteVerse>>() {}.type
        favoriteList = gson.fromJson(json, type)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_section_bible_book_v2, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)

        composeView.setContent {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(favoriteList) {
                    BibleFavoriteVerseComponent(
                        it,
                        modifier = Modifier.clickable {
                            val intent = Intent(requireContext(), requireActivity()::class.java)
                            intent.data =
                                "https://www.aelf.org/bible/${it.bookRef}/${it.chapterRef}?reference=${it.chapterRef},${it.verseRef}".toUri()

                            startActivity(intent)
                        },
                    )
                }
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val activity = activity as? LecturesActivity
        if (parentFragmentManager.backStackEntryCount > 0) {
            activity?.setHomeButtonEnabled(true, View.OnClickListener { v: View? ->
                parentFragmentManager.popBackStack()
            })
        } else {
            activity?.setHomeButtonEnabled(false, null)
        }
    }

    override fun onStop() {
        super.onStop()
        val activity = activity as? LecturesActivity
        // Reset the home button state
        activity?.setHomeButtonEnabled(false, null)
    }

    override fun getRoute(): String? {
        return ""
    }

    override fun getTitle(): String? {
        return ""
    }

}