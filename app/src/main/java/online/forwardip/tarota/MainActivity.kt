package online.forwardip.tarota

import android.Manifest
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private const val SITE = "https://tarota.forwardip.online"

/**
 * Real Google Play Billing is not wired yet. Until it is, the paywall is shown
 * only in debug (test) builds as a demo, and Premium spreads are free in release.
 */
private val PAYWALL_DEMO = BuildConfig.DEBUG

class MainActivity : ComponentActivity() {
    private val askNotif = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = Store(applicationContext)
        setContent {
            Th.systemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val dark = Th.dark
            androidx.compose.runtime.SideEffect {
                val c = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
                c.isAppearanceLightStatusBars = !dark
                c.isAppearanceLightNavigationBars = !dark
            }
            MaterialTheme(colorScheme = if (dark) androidx.compose.material3.darkColorScheme(primary = Terra, background = Paper, surface = Paper, onSurface = Ink) else lightColorScheme(primary = Terra, background = Paper, surface = Paper, onSurface = Ink)) {
                Surface(Modifier.fillMaxSize(), color = Paper) {
                    androidx.compose.material3.ProvideTextStyle(TextStyle(fontFamily = Body, color = Ink, fontSize = 15.sp)) {
                        App(store) { hour ->
                            Notify.schedule(applicationContext, hour)
                            if (hour >= 0 && Build.VERSION.SDK_INT >= 33) askNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
            }
        }
    }
}

private fun unlocked(store: Store, s: Spread) = !s.pro || store.premium || !PAYWALL_DEMO

// ================================================================= shared bits

@Composable
private fun H1(text: String, modifier: Modifier = Modifier, color: Color = Ink, size: Int = 26) = Text(
    text, color = color, fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = size.sp,
    lineHeight = (size * 1.15).sp, modifier = modifier,
)

@Composable
private fun H2(text: String) = Text(
    text, fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 17.sp,
    modifier = Modifier.padding(top = 26.dp, bottom = 12.dp),
)

@Composable
private fun Sub(text: String, modifier: Modifier = Modifier) = Text(text, color = Muted, fontSize = 15.sp, modifier = modifier)

@Composable
private fun Small(text: String, color: Color = Muted, modifier: Modifier = Modifier) = Text(text, color = color, fontSize = 13.sp, modifier = modifier)

@Composable
private fun Pos(text: String) = Text(text.uppercase(), color = Terra, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.2.sp)

@Composable
private fun Btn(text: String, modifier: Modifier = Modifier, bg: Color = Ink, fg: Color = Paper, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier.fillMaxWidth().height(54.dp).alpha(if (enabled) 1f else .35f).clip(RoundedCornerShape(27.dp)).background(bg)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) { Text(text, color = fg, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
}

@Composable
private fun OBtn(text: String, onClick: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(24.dp)).border(1.5.dp, Ink, RoundedCornerShape(24.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
private fun LinkBtn(text: String, color: Color = Terra, onClick: () -> Unit) =
    Text(text, color = color, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onClick() }.padding(vertical = 8.dp))

@Composable
private fun Back(onBack: () -> Unit) = LinkBtn("← " + tr("back"), onClick = onBack)

@Composable
private fun Badge(text: String, modifier: Modifier = Modifier) =
    Text(text, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Paper2).padding(horizontal = 12.dp, vertical = 6.dp))

@Composable
private fun Panel(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    var m = modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(CardBg).border(1.5.dp, LineC, RoundedCornerShape(24.dp))
    if (onClick != null) m = m.clickable { onClick() }
    Column(m.padding(16.dp), content = content)
}

@Composable
private fun Field(value: String, onChange: (String) -> Unit, hint: String, dark: Boolean = false, big: Boolean = false, lines: Int = 1) {
    val bg = if (dark) Ink2 else CardBg
    val fg = if (dark) Paper else Ink
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(fontFamily = Body, color = fg, fontSize = if (big) 17.sp else 15.sp, fontWeight = if (big) FontWeight.SemiBold else FontWeight.Normal),
        cursorBrush = SolidColor(Terra), singleLine = lines == 1, minLines = lines,
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(bg)
            .border(1.5.dp, if (dark) Ink2 else LineC, RoundedCornerShape(16.dp)).padding(16.dp),
        decorationBox = { inner ->
            Box {
                if (value.isEmpty()) Text(hint, color = if (dark) Color(0xFFA69FC4) else Muted, fontSize = if (big) 17.sp else 15.sp)
                inner()
            }
        },
    )
}

@Composable
private fun Disclaimer() = Small(tr("disclaimer"), modifier = Modifier.padding(top = 26.dp))

private fun formatDate(t: Long): String =
    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.forLanguageTag(Lang.current)).format(Date(t))

// ================================================================= root

private enum class Tab(val icon: String, val key: String) {
    HOME("home", "tab_today"), SPREADS("spreads", "tab_spreads"), LEARN("cards", "tab_learn"),
    JOURNAL("journal", "tab_journal"), PROFILE("profile", "profile")
}

@Composable
private fun App(store: Store, onNotif: (Int) -> Unit) {
    Lang.current // recompose everything on language change
    Th.mode
    var paywall by remember { mutableStateOf(false) }
    if (!store.onboarded) {
        Onboarding(store) {
            store.finishOnboarding()
            onNotif(Profile.NOTIF_HOURS[store.notif])
            if (PAYWALL_DEMO && !store.premium) paywall = true
        }
        return
    }
    if (paywall) {
        Paywall(store) { paywall = false }
        return
    }
    MainScreens(store) { paywall = true }
}

// ================================================================= onboarding

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Onboarding(store: Store, onDone: () -> Unit) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    val ctx = LocalContext.current
    BackHandler(enabled = step in 1..5) { step-- }

    @Composable
    fun Shell(progress: Int, content: @Composable ColumnScope.() -> Unit) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding().padding(22.dp)) {
            if (progress > 0) {
                Row(Modifier.fillMaxWidth().padding(bottom = 28.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    repeat(7) { k -> Box(Modifier.weight(1f).height(4.dp).clip(CircleShape).background(if (k < progress) Terra else LineC)) }
                }
            }
            content()
        }
    }

    when (step) {
        0 -> Shell(0) {
            Spacer(Modifier.weight(1f))
            HeroArch(Modifier.align(Alignment.CenterHorizontally).size(200.dp, 272.dp))
            Spacer(Modifier.height(26.dp))
            H1(tr("ob_w_t"), Modifier.fillMaxWidth(), size = 26)
            Sub(tr("ob_w_s"), Modifier.padding(top = 8.dp))
            Row(Modifier.fillMaxWidth().padding(vertical = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                LANGS.forEach { l -> Chip(l.uppercase(), Lang.current == l, small = true) { store.setLang(l) } }
            }
            Spacer(Modifier.weight(1f))
            Btn(tr("start"), bg = Terra, fg = Color.White) { step = 1 }
        }
        1 -> Shell(1) {
            H1(tr("ob_name_t")); Sub(tr("ob_name_s"), Modifier.padding(top = 6.dp, bottom = 16.dp))
            Field(store.name, { store.name = it.take(30) }, tr("name_ph"), big = true)
            Spacer(Modifier.weight(1f))
            Btn(tr("next"), enabled = store.name.isNotBlank()) { store.name = store.name.trim(); store.saveProfile(); step = 2 }
        }
        2 -> Shell(2) {
            H1(tr("ob_birth_t")); Sub(tr("ob_birth_s"), Modifier.padding(top = 6.dp, bottom = 16.dp))
            val b = store.birth ?: LocalDate.of(1998, 1, 1)
            DateWheel(b) { store.birth = it }
            run {
                Spacer(Modifier.height(18.dp))
                Panel {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CardFace(Profile.birthCard(b), false, true, 74.dp)
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Pos(tr("your_sign")); H1(Profile.zodiac(b), size = 18)
                            Spacer(Modifier.height(8.dp))
                            Pos(tr("birth_card")); H1(Deck.name(Profile.birthCard(b)), size = 18)
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Btn(tr("next")) { store.birth = b; store.saveProfile(); step = 3 }
        }
        3 -> Shell(3) {
            H1(tr("ob_focus_t")); Sub(tr("ob_focus_s"), Modifier.padding(top = 6.dp, bottom = 16.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Profile.FOCUS.forEach { (k, n) ->
                    Chip(n[Lang.i], k in store.focus) { store.focus = if (k in store.focus) store.focus - k else store.focus + k }
                }
            }
            Spacer(Modifier.weight(1f))
            Btn(tr("next"), enabled = store.focus.isNotEmpty()) { store.saveProfile(); step = 4 }
        }
        4 -> Shell(4) {
            H1(tr("ob_rel_t")); Sub(tr("ob_rel_s"), Modifier.padding(top = 6.dp, bottom = 16.dp))
            Profile.REL.forEachIndexed { i, r -> Opt(r[Lang.i], store.rel == i) { store.rel = i } }
            Spacer(Modifier.weight(1f))
            Btn(tr("next"), enabled = store.rel >= 0) { store.saveProfile(); step = 5 }
        }
        5 -> Shell(5) {
            H1(tr("ob_notif_t")); Sub(tr("ob_notif_s"), Modifier.padding(top = 6.dp, bottom = 16.dp))
            Profile.NOTIF.forEachIndexed { i, r -> Opt(r[Lang.i], store.notif == i) { store.notif = i } }
            Spacer(Modifier.weight(1f))
            Btn(tr("next")) { store.saveProfile(); step = 6 }
        }
        else -> Shell(6) {
            var shown by remember { mutableIntStateOf(0) }
            LaunchedEffect(Unit) {
                repeat(3) { delay(700); shown++ }
                delay(700); onDone()
            }
            H1("${tr("ob_build_t")} ${store.name}…")
            Spacer(Modifier.height(20.dp))
            listOf("b1", "b2", "b3").forEachIndexed { i, k ->
                Row(Modifier.padding(vertical = 10.dp).alpha(if (i < shown) 1f else .25f), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(24.dp).clip(CircleShape).background(Terra), contentAlignment = Alignment.Center) { Text("✓", color = Color.White, fontSize = 13.sp) }
                    Spacer(Modifier.width(12.dp)); Text(tr(k), fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.weight(1f))
            Box(Modifier.align(Alignment.CenterHorizontally)) { CardFace(0, false, false, 120.dp) }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun Chip(text: String, on: Boolean, small: Boolean = false, onClick: () -> Unit) {
    Text(
        text, color = if (on) Paper else Ink, fontWeight = FontWeight.SemiBold, fontSize = if (small) 13.sp else 15.sp,
        modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(if (on) Ink else Color.Transparent)
            .border(1.5.dp, Ink, RoundedCornerShape(22.dp)).clickable { onClick() }
            .padding(horizontal = if (small) 12.dp else 16.dp, vertical = if (small) 7.dp else 11.dp),
    )
}

@Composable
private fun Opt(text: String, on: Boolean, onClick: () -> Unit) {
    Text(
        text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(16.dp)).background(CardBg)
            .border(if (on) 2.5.dp else 1.5.dp, if (on) Ink else LineC, RoundedCornerShape(16.dp)).clickable { onClick() }.padding(16.dp),
    )
}

// ================================================================= paywall (demo)

@Composable
private fun Paywall(store: Store, onClose: () -> Unit) {
    var plan by remember { mutableIntStateOf(1) }
    Box(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp)) {
            HeroArch(Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp).size(140.dp, 190.dp))
            H1(tr("pw_t"), Modifier.fillMaxWidth().padding(vertical = 16.dp), size = 24)
            listOf("pw1", "pw2", "pw3", "pw4").forEach { k ->
                Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(22.dp).clip(CircleShape).background(Sage), contentAlignment = Alignment.Center) { Text("✓", color = Color.White, fontSize = 12.sp) }
                    Spacer(Modifier.width(10.dp)); Text(tr(k), fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(14.dp))
            Plan(tr("month"), tr("month_p"), null, plan == 0) { plan = 0 }
            Plan(tr("q3"), tr("q3_p"), tr("best_q3"), plan == 1) { plan = 1 }
            Plan(tr("year"), tr("year_p"), null, plan == 2) { plan = 2 }
            Btn(if (plan == 1) tr("pw_cta") else tr("pw_cta2"), bg = Terra, fg = Color.White) { store.setPremiumDemo(true); onClose() }
            Small(tr("pw_note"), modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
            Small("TEST BUILD · DEMO", color = Terra, modifier = Modifier.padding(top = 6.dp))
        }
        Text("×", fontSize = 30.sp, color = Muted, modifier = Modifier.align(Alignment.TopEnd).clickable { onClose() }.padding(horizontal = 20.dp, vertical = 8.dp))
    }
}

@Composable
private fun Plan(title: String, price: String, tag: String?, on: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(18.dp)).background(CardBg)
            .border(if (on) 2.5.dp else 1.5.dp, if (on) Terra else LineC, RoundedCornerShape(18.dp)).clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold); Small(price) }
        if (tag != null) Text(tag, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Terra).padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

// ================================================================= main

private class Nav {
    var tab by mutableStateOf(Tab.HOME)
    var spread by mutableStateOf<Spread?>(null)
    var question by mutableStateOf("")
    var autoDraw by mutableStateOf(false)
    var card by mutableStateOf<Int?>(null)
    var reading by mutableStateOf<Reading?>(null)
    var lesson by mutableStateOf<Int?>(null)
    var quiz by mutableStateOf(false)
    var allCards by mutableStateOf(false)
    fun reset() { spread = null; card = null; reading = null; autoDraw = false; question = ""; lesson = null; quiz = false; allCards = false }
}

@Composable
private fun MainScreens(store: Store, openPaywall: () -> Unit) {
    val nav = remember { Nav() }
    BackHandler(enabled = nav.spread != null || nav.card != null || nav.reading != null || nav.lesson != null || nav.quiz || nav.allCards) {
        when {
            nav.card != null -> nav.card = null
            nav.lesson != null -> nav.lesson = null
            nav.quiz -> nav.quiz = false
            nav.allCards -> nav.allCards = false
            nav.reading != null -> nav.reading = null
            else -> { nav.spread = null; nav.autoDraw = false }
        }
    }
    val openSpread: (Spread) -> Unit = { s -> if (unlocked(store, s)) { nav.spread = s; nav.question = ""; nav.autoDraw = false } else openPaywall() }

    Box(Modifier.fillMaxSize().statusBarsPadding()) {
        Box(Modifier.fillMaxSize()) {
            when {
                nav.card != null -> Screen { CardDetail(nav.card!!) { nav.card = null } }
                nav.lesson != null -> Screen { LessonView(nav.lesson!!, { nav.lesson = it }) { nav.lesson = null } }
                nav.quiz -> Screen { Quiz { nav.quiz = false } }
                nav.allCards -> CardGrid({ nav.allCards = false }) { nav.card = it }
                nav.reading != null -> Screen { ReadingView(nav.reading!!) { nav.reading = null } }
                nav.spread != null -> Screen { SpreadScreen(store, nav.spread!!, nav.question, nav.autoDraw) { nav.spread = null; nav.autoDraw = false } }
                else -> when (nav.tab) {
                    Tab.HOME -> Screen { Home(store, nav, openSpread) }
                    Tab.SPREADS -> Screen { H1(tr("all_spreads")); Spacer(Modifier.height(14.dp)); SpreadTiles(store, Spreads.all, openSpread) }
                    Tab.LEARN -> Screen { LearnHome(nav) }
                    Tab.JOURNAL -> Screen { JournalList(store) { nav.reading = it } }
                    Tab.PROFILE -> Screen { ProfileScreen(store, openPaywall) }
                }
            }
        }
        NavPill(nav.tab, Modifier.align(Alignment.BottomCenter)) { nav.tab = it; nav.reset() }
    }
}

@Composable
private fun Screen(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 120.dp), content = content)
}

@Composable
private fun NavPill(current: Tab, modifier: Modifier, onSelect: (Tab) -> Unit) {
    Row(
        modifier.navigationBarsPadding().padding(14.dp).fillMaxWidth().height(64.dp).clip(RoundedCornerShape(32.dp)).background(if (Th.dark) Color(0xFF2A2440) else TileInk).padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically,
    ) {
        Tab.entries.forEach { t ->
            val on = t == current
            Column(
                Modifier.size(60.dp, 52.dp).clip(RoundedCornerShape(26.dp)).background(if (on) Terra else Color.Transparent).clickable { onSelect(t) },
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            ) {
                val c = if (on) Color.White else Color(0xFFCFC6E8)
                LineIcon(t.icon, c, 21.dp, 4.2f)
                Text(tr(t.key), color = c, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }
    }
}

// ---------------------------------------------------------------- home

@Composable
private fun Home(store: Store, nav: Nav, openSpread: (Spread) -> Unit) {
    val daily = remember { store.dailyCard() }
    var open by remember { mutableStateOf(store.dailyOpened) }
    var q by remember { mutableStateOf("") }
    val h = LocalTime.now().hour
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Small(tr(if (h < 12) "hi_m" else if (h < 18) "hi_d" else "hi_e") + ",")
            H1(store.name)
        }
        Badge("🔥 ${store.streak()} ${tr("streak")}")
    }
    store.birth?.let { Badge("${Profile.zodiac(it)} · ${Deck.name(Profile.birthCard(it))}", Modifier.padding(top = 6.dp)) }

    Column(Modifier.padding(top = 18.dp).fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Ink).padding(20.dp)) {
        H1(tr("ask_t"), color = Paper, size = 18)
        Spacer(Modifier.height(12.dp))
        Field(q, { q = it.take(300) }, tr("ask_ph"), dark = true, lines = 2)
        Spacer(Modifier.height(12.dp))
        Btn(tr("ask_btn"), bg = Terra, fg = Color.White) {
            nav.spread = Spreads.byId("three"); nav.question = q.trim(); nav.autoDraw = true; q = ""
        }
    }

    H2(tr("card_of_day"))
    Panel(onClick = { if (open) nav.card = daily.card else { open = true; store.dailyOpened = true; store.markDay() } }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CardFace(daily.card, daily.reversed, open, 86.dp)
            Spacer(Modifier.width(16.dp))
            Column {
                if (open) {
                    H1(Deck.name(daily.card), size = 17)
                    if (daily.reversed) Small(tr("reversed"))
                    Text(Deck.meaning(daily.card, daily.reversed), fontSize = 14.sp, modifier = Modifier.padding(top = 6.dp))
                } else {
                    H1(tr("daily_open"), size = 17)
                    Small(tr("daily_hint"), modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
    }

    H2(tr("quick"))
    val ordered = Spreads.all.sortedByDescending { (it.id == "love" && "love" in store.focus) || (it.id == "career" && ("career" in store.focus || "money" in store.focus)) }
    SpreadTiles(store, ordered, openSpread)
    Disclaimer()
}

@Composable
private fun SpreadTiles(store: Store, list: List<Spread>, onOpen: (Spread) -> Unit) {
    list.chunked(2).forEach { row ->
        Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            row.forEach { s ->
                val dark = s.color == 0xFF1F1B33
                val fg = if (dark) TilePaper else TileInk
                Column(
                    Modifier.weight(1f).heightIn(min = 108.dp).clip(RoundedCornerShape(20.dp)).background(Color(s.color)).clickable { onOpen(s) }.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    LineIcon(s.icon, fg, 28.dp, 3.6f)
                    Spacer(Modifier.height(14.dp))
                    Text(s.title(), color = fg, fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 17.sp)
                    val lock = if (!unlocked(store, s)) " · ★ ${tr("locked")}" else ""
                    Text("${s.size} ${tr("cards_count")}$lock", color = fg.copy(alpha = .7f), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
    }
}

// ---------------------------------------------------------------- reading

@Composable
private fun SpreadScreen(store: Store, spread: Spread, initialQ: String, autoDraw: Boolean, onBack: () -> Unit) {
    var question by remember { mutableStateOf(initialQ) }
    var current by remember {
        mutableStateOf(
            if (autoDraw) Reading(System.currentTimeMillis(), spread.id, initialQ, store.draw(spread.size)).also { store.save(it) } else null
        )
    }
    Back(onBack)
    H1(spread.title(), Modifier.padding(bottom = 12.dp))
    val r = current
    if (r == null) {
        Field(question, { question = it.take(300) }, tr("question_hint"), lines = 3)
        Spacer(Modifier.height(16.dp))
        Btn(tr("draw")) {
            val reading = Reading(System.currentTimeMillis(), spread.id, question.trim(), store.draw(spread.size))
            store.save(reading); current = reading
        }
    } else {
        ReadingBody(r, startHidden = true)
        Small(tr("saved"), modifier = Modifier.padding(vertical = 10.dp))
        OBtn(tr("again")) { current = null; question = "" }
    }
    Disclaimer()
}

@Composable
private fun ReadingBody(r: Reading, startHidden: Boolean) {
    val spread = Spreads.byId(r.spreadId) ?: return
    val revealed = remember(r) { mutableStateListOf(*Array(r.cards.size) { !startHidden }) }
    if (r.question.isNotBlank()) Text("“${r.question}”", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 10.dp))
    if (spread.id == "yesno" && revealed.all { it }) {
        val d = r.cards.first()
        val pol = Deck.polarity(d.card) * if (d.reversed) -1 else 1
        AnimatedVisibility(true, enter = fadeIn()) {
            Text(
                tr(if (pol > 0) "yes" else if (pol < 0) "no" else "maybe"), color = Terra, fontFamily = Display,
                fontWeight = FontWeight.SemiBold, fontSize = 46.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            )
        }
    }
    if (spread.id == "triplet" && revealed.all { it }) {
        Panel(Modifier.padding(bottom = 6.dp)) {
            Pos(tr("trip_sum"))
            Text(Deck.tripletSummary(r.cards), fontSize = 15.sp, lineHeight = 21.sp, modifier = Modifier.padding(top = 6.dp))
        }
    }
    r.cards.forEachIndexed { i, d ->
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            CardFace(d.card, d.reversed, revealed[i], 92.dp) { revealed[i] = true }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Pos(spread.position(i))
                if (revealed[i]) {
                    H1(Deck.name(d.card), Modifier.padding(top = 2.dp), size = 18)
                    if (d.reversed) Small(tr("reversed"))
                    Text(Deck.meaning(d.card, d.reversed), modifier = Modifier.padding(top = 6.dp))
                } else {
                    Sub(tr("tap_reveal"), Modifier.padding(top = 6.dp))
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(LineC))
    }
}

@Composable
private fun ReadingView(r: Reading, onBack: () -> Unit) {
    Back(onBack)
    H1(Spreads.byId(r.spreadId)?.title() ?: "")
    Small(formatDate(r.time), modifier = Modifier.padding(bottom = 8.dp))
    ReadingBody(r, startHidden = false)
}

// ---------------------------------------------------------------- cards

@Composable
private fun CardGrid(onBack: () -> Unit, onOpen: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(84.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { Column { Back(onBack); H1(tr("all_cards")) } }
        items(Deck.SIZE) { id -> CardFace(id, false, true, null) { onOpen(id) } }
    }
}

@Composable
private fun CardDetail(id: Int, onBack: () -> Unit) {
    Back(onBack)
    Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) { CardFace(id, false, true, 180.dp) }
    H1(Deck.name(id), Modifier.padding(top = 10.dp), size = 22)
    Small(if (Deck.isMajor(id)) tr("major") else tr("minor"))
    Spacer(Modifier.height(18.dp)); Pos(tr("upright")); Text(Deck.meaning(id, false))
    Spacer(Modifier.height(14.dp)); Pos(tr("reversed")); Text(Deck.meaning(id, true))
}

// ---------------------------------------------------------------- journal

@Composable
private fun JournalList(store: Store, onOpen: (Reading) -> Unit) {
    H1(tr("tab_journal"))
    if (store.journal.isEmpty()) { Sub(tr("journal_empty")); return }
    store.journal.toList().forEach { r ->
        Panel(Modifier.padding(top = 10.dp), onClick = { onOpen(r) }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) { Pos(Spreads.byId(r.spreadId)?.title() ?: "") }
                Small(tr("delete"), modifier = Modifier.clickable { store.delete(r) }.padding(4.dp))
            }
            Small(formatDate(r.time), modifier = Modifier.padding(top = 2.dp))
            if (r.question.isNotBlank()) Text("“${r.question}”", fontWeight = FontWeight.SemiBold, maxLines = 2, modifier = Modifier.padding(top = 4.dp))
            Small(r.cards.joinToString(" · ") { Deck.name(it.card) }, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

// ---------------------------------------------------------------- profile

@Composable
private fun ProfileScreen(store: Store, openPaywall: () -> Unit) {
    val uri = LocalUriHandler.current
    var reversed by remember { mutableStateOf(store.useReversed) }
    H1(tr("profile"))
    Spacer(Modifier.height(8.dp))
    Panel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            store.birth?.let { CardFace(Profile.birthCard(it), false, true, 64.dp); Spacer(Modifier.width(16.dp)) }
            Column {
                H1(store.name, size = 18)
                store.birth?.let { Small("${Profile.zodiac(it)} · ${Deck.name(Profile.birthCard(it))}") }
                Badge(if (store.premium) "★ Premium" else "Free", Modifier.padding(top = 8.dp))
            }
        }
    }
    if (PAYWALL_DEMO && !store.premium) { Spacer(Modifier.height(14.dp)); Btn(tr("premium"), bg = Terra, fg = Color.White) { openPaywall() } }
    H2(tr("theme"))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("theme_sys", "theme_light", "theme_dark").forEachIndexed { i, k -> Chip(tr(k), Th.mode == i, small = true) { store.setTheme(i) } }
    }
    H2(tr("language"))
    LANGS.forEach { l ->
        Row(Modifier.fillMaxWidth().clickable { store.setLang(l) }, verticalAlignment = Alignment.CenterVertically) {
            RadioButton(Lang.current == l, { store.setLang(l) }, colors = RadioButtonDefaults.colors(selectedColor = Terra))
            Text(LANG_NAMES[l] ?: l, fontWeight = FontWeight.SemiBold)
        }
    }
    Box(Modifier.fillMaxWidth().padding(vertical = 14.dp).height(1.dp).background(LineC))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) { Text(tr("use_reversed"), fontWeight = FontWeight.SemiBold); Small(tr("use_reversed_hint")) }
        Switch(reversed, { reversed = it; store.useReversed = it }, colors = SwitchDefaults.colors(checkedTrackColor = Terra))
    }
    Box(Modifier.fillMaxWidth().padding(vertical = 14.dp).height(1.dp).background(LineC))
    listOf("privacy" to "privacy.html", "terms" to "terms.html", "support" to "support.html").forEach { (k, page) ->
        LinkBtn(tr(k)) { uri.openUri("$SITE/$page?lang=${Lang.current}") }
    }
    LinkBtn(tr("clear_journal"), Muted) { store.clear() }
    LinkBtn(tr("redo"), Muted) { store.setPremiumDemo(false); store.resetOnboarding() }
    Disclaimer()
    Small("Tarota ${BuildConfig.VERSION_NAME}", modifier = Modifier.padding(top = 6.dp))
}

// ================================================================= wheel date picker

private val WHEEL_ITEM = 44.dp

@Composable
private fun DateWheel(value: LocalDate, onChange: (LocalDate) -> Unit) {
    val years = (1930..LocalDate.now().year).toList().reversed()
    val months = (1..12).map { m ->
        java.time.Month.of(m).getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, Locale.forLanguageTag(Lang.current))
            .replaceFirstChar { it.uppercase() }
    }
    val maxDay = java.time.YearMonth.of(value.year, value.monthValue).lengthOfMonth()
    val days = (1..31).toList()
    fun set(y: Int = value.year, m: Int = value.monthValue, d: Int = value.dayOfMonth) {
        val last = java.time.YearMonth.of(y, m).lengthOfMonth()
        val nd = LocalDate.of(y, m, d.coerceAtMost(last))
        if (nd != value) onChange(if (nd.isAfter(LocalDate.now())) LocalDate.now() else nd)
    }
    Box(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(CardBg).border(1.5.dp, LineC, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center,
    ) {
        // selection band
        Box(Modifier.fillMaxWidth().padding(horizontal = 10.dp).height(WHEEL_ITEM).clip(RoundedCornerShape(12.dp)).background(Paper2))
        Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
            Wheel(days.map { it.toString() }, value.dayOfMonth - 1, Modifier.weight(.8f), disabledFrom = maxDay) { set(d = it + 1) }
            Wheel(months, value.monthValue - 1, Modifier.weight(1.6f)) { set(m = it + 1) }
            Wheel(years.map { it.toString() }, years.indexOf(value.year), Modifier.weight(1f)) { set(y = years[it]) }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun Wheel(items: List<String>, selected: Int, modifier: Modifier, disabledFrom: Int = Int.MAX_VALUE, onSelect: (Int) -> Unit) {
    val state = androidx.compose.foundation.lazy.rememberLazyListState(initialFirstVisibleItemIndex = selected.coerceAtLeast(0))
    val fling = androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior(lazyListState = state)
    val density = androidx.compose.ui.platform.LocalDensity.current
    val itemPx = with(density) { WHEEL_ITEM.toPx() }
    val center by androidx.compose.runtime.remember {
        androidx.compose.runtime.derivedStateOf {
            state.firstVisibleItemIndex + if (state.firstVisibleItemScrollOffset > itemPx / 2) 1 else 0
        }
    }
    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            val idx = center.coerceIn(0, items.lastIndex)
            if (idx >= disabledFrom) state.animateScrollToItem(disabledFrom - 1)
            else if (idx != selected) onSelect(idx)
        }
    }
    LaunchedEffect(selected) {
        if (!state.isScrollInProgress && center != selected && selected >= 0) state.scrollToItem(selected)
    }
    androidx.compose.foundation.lazy.LazyColumn(
        state = state, flingBehavior = fling, modifier = modifier.height(WHEEL_ITEM * 5),
        contentPadding = PaddingValues(vertical = WHEEL_ITEM * 2), horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items.size) { i ->
            val dist = kotlin.math.abs(i - center)
            Box(Modifier.height(WHEEL_ITEM).fillMaxWidth().clickable { onSelect(i) }, contentAlignment = Alignment.Center) {
                Text(
                    items[i],
                    fontFamily = if (dist == 0) Display else Body,
                    fontWeight = if (dist == 0) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = if (dist == 0) 18.sp else 15.sp,
                    color = when { i >= disabledFrom -> LineC; dist == 0 -> Ink; dist == 1 -> Muted; else -> LineC },
                    maxLines = 1,
                )
            }
        }
    }
}

// ================================================================= learning

@Composable
private fun LearnHome(nav: Nav) {
    H1(tr("tab_learn"))
    H2(tr("practice"))
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Terra).clickable { nav.quiz = true }.padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(tr("quiz_t"), color = Color.White, fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 22.sp)
                Text(tr("quiz_s"), color = Color.White.copy(alpha = .85f), fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp))
            }
            Spacer(Modifier.width(12.dp))
            CardFace(17, false, false, 56.dp)
        }
    }
    H2(tr("lessons"))
    Learn.lessons.forEachIndexed { i, l ->
        Panel(Modifier.padding(bottom = 10.dp), onClick = { nav.lesson = i }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(Paper2), contentAlignment = Alignment.Center) { LineIcon(l.icon, Terra, 22.dp, 4f) }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Small("${tr("lesson")} ${i + 1}")
                    Text(l.title[Lang.i], fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Text("→", color = Terra, fontSize = 18.sp)
            }
        }
    }
    H2(tr("all_cards"))
    Row(Modifier.fillMaxWidth().clickable { nav.allCards = true }, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf(0, 19, 23, 36).forEach { id -> Box(Modifier.weight(1f)) { CardFace(id, false, true, null) { nav.allCards = true } } }
    }
    Spacer(Modifier.height(12.dp))
    OBtn(tr("all_cards") + " →") { nav.allCards = true }
}

@Composable
private fun LessonView(i: Int, open: (Int) -> Unit, onBack: () -> Unit) {
    val l = Learn.lessons[i]
    Back(onBack)
    Small("${tr("lesson")} ${i + 1} / ${Learn.lessons.size}")
    H1(l.title[Lang.i], Modifier.padding(top = 4.dp, bottom = 8.dp))
    l.body[Lang.i].split("\n\n").forEach { para ->
        Text(para, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(top = 12.dp))
    }
    Spacer(Modifier.height(24.dp))
    if (i + 1 < Learn.lessons.size) {
        Btn("${tr("lesson")} ${i + 2}: ${Learn.lessons[i + 1].title[Lang.i]} →") { open(i + 1) }
    } else {
        OBtn(tr("back")) { onBack() }
    }
}

@Composable
private fun Quiz(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var total by remember { mutableIntStateOf(0) }
    val card = remember(round) { (0 until Deck.SIZE).random() }
    val options = remember(round) {
        val others = (0 until Deck.SIZE).filter { it != card && Deck.meaning(it, false) != Deck.meaning(card, false) }.shuffled().take(2)
        (others + card).shuffled()
    }
    var picked by remember(round) { mutableStateOf<Int?>(null) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.weight(1f)) { Back(onBack) }
        Badge("${tr("score")}: $score / $total")
    }
    H1(tr("quiz_q"), Modifier.padding(vertical = 10.dp), size = 20)
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CardFace(card, false, true, 150.dp) }
    Text(Deck.name(card), fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
    options.forEach { o ->
        val p = picked
        val bg = when {
            p == null -> CardBg
            o == card -> Sage.copy(alpha = .25f)
            o == p -> Terra.copy(alpha = .2f)
            else -> CardBg
        }
        val border = when { p != null && o == card -> Sage; p != null && o == p -> Terra; else -> LineC }
        Text(
            Deck.meaning(o, false), fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(16.dp)).background(bg)
                .border(1.5.dp, border, RoundedCornerShape(16.dp))
                .clickable(enabled = p == null) { picked = o; total++; if (o == card) score++ }.padding(16.dp),
        )
    }
    picked?.let { p ->
        Text(
            if (p == card) tr("correct") else tr("wrong"), color = if (p == card) Sage else Terra,
            fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp),
        )
        Btn(tr("next_card")) { round++ }
    }
}
