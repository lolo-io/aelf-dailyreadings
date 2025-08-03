package co.epitre.aelf_lectures.bible.biblebookfragment.components

import android.os.SystemClock
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import co.epitre.aelf_lectures.bible.data.BibleVerse
import co.epitre.aelf_lectures.compose.theme.Typo
import co.epitre.aelf_lectures.compose.theme.colors
import co.epitre.aelf_lectures.compose.theme.spacing
import co.epitre.aelf_lectures.compose.utils.Space
import co.epitre.aelf_lectures.compose.utils.ifThen

@Composable
fun BibleVerseComponent(
    ref: String,
    text: String,
    zoom: Float,
    modifier: Modifier = Modifier,
    isFocused: Boolean = false,
    isFavorite: Boolean = false,
    isHighlighted: Boolean = false,
    searchQuery: String? = null,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {}
) {
    val viewConfiguration = androidx.compose.ui.platform.LocalViewConfiguration.current
    val doubleTapTimeout = android.view.ViewConfiguration.getDoubleTapTimeout()
    val touchSlop = viewConfiguration.touchSlop
    var lastTapTime by remember { mutableLongStateOf(0L) }
    var lastTapPos by remember { androidx.compose.runtime.mutableStateOf<androidx.compose.ui.geometry.Offset?>(null) }

    Column(
        modifier
            .height(IntrinsicSize.Max)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val downPos = down.position
                    var isTap = true

                    // Detect movement beyond slop
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()
                        if (!change.pressed) break
                        if ((change.position - downPos).getDistance() > touchSlop) {
                            isTap = false
                            break
                        }
                    }

                    if (isTap) {
                        val now = System.currentTimeMillis()
                        if (lastTapTime != 0L &&
                            now - lastTapTime < doubleTapTimeout &&
                            lastTapPos?.let { (it - downPos).getDistance() < touchSlop } == true
                        ) {
                            onDoubleClick()
                            lastTapTime = 0L
                            lastTapPos = null
                        } else {
                            onClick()
                            lastTapTime = now
                            lastTapPos = downPos
                        }
                    }
                }
            }
    ) {
        Row {
            DisableSelection {
                TextWithZoom(
                    ref,
                    color = colors.textAnnotation,
                    style = Typo.verse,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(top = 1.dp * zoom)
                        .widthIn((12 * zoom).dp),
                    zoom = zoom
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 1.dp, end = 3.dp)
                    .fillMaxHeight()
                    .width(1.5.dp)
                    .background(if (isFocused) colors.focusText else Color.Transparent)
            )

            val style = if (isFavorite) Typo.bodyFavorite else Typo.body
            val color = if(isFavorite) colors.textFavorite else colors.textNeutral

            if (searchQuery != null && searchQuery.isNotEmpty()) {
                TextWithZoomAndHighlights(
                    text,
                    color = color,
                    searchRegex = searchQuery,
                    style = style,
                    modifier = Modifier
                        .alignByBaseline()
                        .ifThen(isHighlighted) { background(colors.highlightBackground) },
                    zoom = zoom
                )
            } else {
                TextWithZoom(
                    text,
                    color = color,
                    style = style,
                    modifier = Modifier
                        .alignByBaseline()
                        .ifThen(isHighlighted) { background(colors.highlightBackground) },
                    zoom = zoom
                )
            }

        }
        Space(spacing.s38)
    }
}


val previewBibleVerse = BibleVerse(
    "1",
    "AU COMMENCEMENT, Dieu créa le ciel et la terre."
)


@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewBibleVerse(
    ref: String = previewBibleVerse.ref,
    text: String = previewBibleVerse.text
) {
    BibleVerseComponent(
        ref = ref,
        text = text,
        zoom = 1f,
        isFocused = true,
        isHighlighted = true
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewBibleVerse2(
    ref: String = previewBibleVerse.ref,
    text: String = previewBibleVerse.text
) {
    PreviewBibleVerse("12")
}

@Composable
fun Int.charsWidth(fontSize: TextUnit): Dp {
    val density = LocalDensity.current
    return with(density) {
        (this@charsWidth * fontSize.toPx() * 0.6f).toDp()
    }
}
