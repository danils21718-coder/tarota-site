package online.forwardip.tarota

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// ---------------------------------------------------------------- palette & type
object Th {
    /** 0 = system, 1 = light, 2 = dark */
    var mode by mutableStateOf(0)
    var systemDark by mutableStateOf(false)
    val dark: Boolean get() = mode == 2 || (mode == 0 && systemDark)
}
private fun c(light: Long, dark: Long) = Color(if (Th.dark) dark else light)

val Paper: Color get() = c(0xFFF4ECE0, 0xFF15121F)
val Paper2: Color get() = c(0xFFEADFCF, 0xFF26213A)
val CardBg: Color get() = c(0xFFFFFAF2, 0xFF1E1A2E)
val Ink: Color get() = c(0xFF1F1B33, 0xFFF1EADF)
val Ink2: Color get() = c(0xFF2C2745, 0xFFDCD3C6)
val Terra: Color get() = c(0xFFC2552F, 0xFFD9683F)
val Terra2 = Color(0xFFE6A07A)
val Sage = Color(0xFF6E8B74)
val Muted: Color get() = c(0xFF7A6F66, 0xFFA59CB8)
val LineC: Color get() = c(0xFFDCCFBD, 0xFF332D48)
/** Fixed colours for pastel tiles (same in both themes). */
val TileInk = Color(0xFF1F1B33)
val TilePaper = Color(0xFFF4ECE0)

val Display = FontFamily(Font(R.font.display_semibold, FontWeight.SemiBold))
val Body = FontFamily(
    Font(R.font.body_regular, FontWeight.Normal),
    Font(R.font.body_semibold, FontWeight.SemiBold),
    Font(R.font.body_bold, FontWeight.Bold),
)

/** Arch-topped card silhouette. */
val ArchShape: Shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomStartPercent = 8, bottomEndPercent = 8)

// ---------------------------------------------------------------- icons (24×24 line art)
private val ICONS = mapOf(
    "home" to "M4 11l8-7 8 7v9h-5v-6H9v6H4z",
    "spreads" to "M6 6a3 3 0 0 1 3 3v6a3 3 0 0 1-6 0V9a3 3 0 0 1 3-3zM12 4a3 3 0 0 1 3 3v8a3 3 0 0 1-6 0V7a3 3 0 0 1 3-3zM18 6a3 3 0 0 1 3 3v6a3 3 0 0 1-6 0V9a3 3 0 0 1 3-3z",
    "cards" to "M4 4h7v7H4zM13 4h7v7h-7zM4 13h7v7H4zM13 13h7v7h-7z",
    "journal" to "M6 3h11a1 1 0 0 1 1 1v17l-6.5-4L5 21V4a1 1 0 0 1 1-1z",
    "profile" to "M12 4a4 4 0 1 1 0 8a4 4 0 1 1 0-8zM4 21c1-4.5 4.5-6.5 8-6.5s7 2 8 6.5",
    "yes" to "M12 3a9 9 0 1 1 0 18a9 9 0 1 1 0-18zM8 12l3 3 5-6",
    "heart" to "M12 20s-7-4.4-7-10a4 4 0 0 1 7-2.6A4 4 0 0 1 19 10c0 5.6-7 10-7 10z",
    "moon" to "M15 3a9 9 0 1 0 0 18A7 7 0 0 1 15 3z",
    "leaf" to "M5 19C5 10 11 5 20 4c-1 9-6 15-15 15zM5 19l8-8",
    "cross" to "M12 3v18M5 9h14",
)

// suit glyphs on a 100×100 grid
private val SUIT_ART = listOf(
    "M50 92V18M50 32c-10-2-14-10-14-10M50 46c10-2 14-10 14-10M50 60c-10-2-14-10-14-10M50 10a4 4 0 1 1 0 8a4 4 0 1 1 0-8z",
    "M28 26h44c0 22-10 34-22 34S28 48 28 26zM50 60v20M36 84h28M28 26c6 4 38 4 44 0",
    "M50 10v62M38 72h24M50 72v18M50 10l-5 10M50 10l5 10",
    "M50 18a32 32 0 1 1 0 64a32 32 0 1 1 0-64zM50 24l7.6 23.4H82L62.2 61.8 69.8 85 50 70.6 30.2 85l7.6-23.2L18 47.4h24.4z",
)

private val pathCache = HashMap<String, Path>()
private fun path(d: String): Path = pathCache.getOrPut(d) { PathParser().parsePathString(d).toPath() }

private fun DrawScope.strokePath(d: String, grid: Float, color: Color, width: Float) {
    val s = size.minDimension / grid
    translate((size.width - grid * s) / 2f, (size.height - grid * s) / 2f) {
        scale(s, s, pivot = Offset.Zero) {
            drawPath(path(d), color, style = Stroke(width / s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

@Composable
fun LineIcon(name: String, color: Color, size: Dp = 22.dp, width: Float = 4.5f) {
    Canvas(Modifier.size(size)) { strokePath(ICONS[name] ?: "", 24f, color, width) }
}

/** Sun with rays — used for majors and the card back. */
private fun DrawScope.sun(color: Color, cx: Float, cy: Float, r: Float, rays: Int, w: Float) {
    drawCircle(color, r, Offset(cx, cy), style = Stroke(w))
    for (i in 0 until rays) {
        val a = i * 2 * Math.PI / rays
        val r1 = r * 1.4f
        val r2 = if (i % 2 == 0) r * 2.6f else r * 2.1f
        drawLine(
            color, Offset(cx + r1 * cos(a).toFloat(), cy + r1 * sin(a).toFloat()),
            Offset(cx + r2 * cos(a).toFloat(), cy + r2 * sin(a).toFloat()), w, StrokeCap.Round,
        )
    }
}

// ---------------------------------------------------------------- card scans (bundled at build time)
private val bitmapCache = android.util.LruCache<Int, ImageBitmap>(24)
private val missing = HashSet<Int>()
private fun loadScan(ctx: Context, id: Int): ImageBitmap? {
    bitmapCache.get(id)?.let { return it }
    if (id in missing) return null
    val bmp = runCatching { ctx.assets.open("cards/$id.jpg").use { BitmapFactory.decodeStream(it)?.asImageBitmap() } }.getOrNull()
    if (bmp == null) missing.add(id) else bitmapCache.put(id, bmp)
    return bmp
}

private val ROMAN = listOf("0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI")
private val RANK_SHORT = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "P", "Kn", "Q", "K")

@Composable
fun CardFace(id: Int, reversed: Boolean, faceUp: Boolean, width: Dp?, onClick: (() -> Unit)? = null) {
    var m: Modifier = if (width != null) Modifier.size(width, width / 0.6f) else Modifier.fillMaxWidth().aspectRatio(0.6f)
    if (!faceUp) {
        m = m.clip(ArchShape).background(Terra)
        if (onClick != null) m = m.clickable { onClick() }
        Box(m) {
            Canvas(Modifier.fillMaxSize().padding(5.dp)) {
                val w = size.width; val h = size.height
                drawRoundRect(TilePaper.copy(alpha = .6f), style = Stroke(1.5f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w / 2, w / 2))
                sun(TilePaper.copy(alpha = .9f), w / 2, h * .47f, w * .16f, 16, w * .012f + 1f)
                drawLine(TilePaper.copy(alpha = .8f), Offset(w * .3f, h * .84f), Offset(w * .7f, h * .84f), 2f)
                drawLine(TilePaper.copy(alpha = .8f), Offset(w * .38f, h * .89f), Offset(w * .62f, h * .89f), 2f)
            }
        }
        return
    }
    val ctx = LocalContext.current
    val scan = remember(id) { loadScan(ctx, id) }
    val shape = RoundedCornerShape(10.dp)
    m = m.clip(shape).background(CardBg).border(1.dp, Ink, shape)
    if (onClick != null) m = m.clickable { onClick() }
    Box(m.rotate(if (reversed) 180f else 0f)) {
        if (scan != null) {
            Image(scan, Deck.name(id), Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } else {
            Text(
                if (Deck.isMajor(id)) ROMAN[id] else RANK_SHORT[Deck.rank(id)],
                color = Terra, fontFamily = Display, fontSize = 11.sp,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp),
            )
            Canvas(Modifier.align(Alignment.Center).fillMaxWidth(.62f).aspectRatio(1f)) {
                if (Deck.isMajor(id)) sun(Ink, size.width / 2, size.height / 2, size.width * .14f, 12, 2.4f)
                else strokePath(SUIT_ART[Deck.suit(id)], 100f, Ink, 2.6f)
            }
            Text(
                Deck.name(id), color = Ink, fontFamily = Body, fontWeight = FontWeight.Bold, fontSize = 10.sp,
                textAlign = TextAlign.Center, maxLines = 2, lineHeight = 12.sp,
                modifier = Modifier.align(Alignment.BottomCenter).padding(5.dp),
            )
        }
    }
}

/** Big arch with sunrise — onboarding and paywall hero. */
@Composable
fun HeroArch(modifier: Modifier) {
    Box(modifier.clip(ArchShape).background(Terra)) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width; val h = size.height
            sun(TilePaper, w / 2, h * .47f, w * .155f, 24, 2.2f)
            val p1 = Path().apply { moveTo(w * .18f, h * .83f); quadraticTo(w * .5f, h * .73f, w * .82f, h * .83f) }
            val p2 = Path().apply { moveTo(w * .27f, h * .9f); quadraticTo(w * .5f, h * .84f, w * .73f, h * .9f) }
            drawPath(p1, TilePaper, style = Stroke(2.2f, cap = StrokeCap.Round))
            drawPath(p2, TilePaper, style = Stroke(2.2f, cap = StrokeCap.Round))
        }
    }
}
