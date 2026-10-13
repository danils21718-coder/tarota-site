package online.forwardip.tarota

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import kotlin.random.Random

data class Drawn(val card: Int, val reversed: Boolean)

data class Reading(
    val time: Long,
    val spreadId: String,
    val question: String,
    val cards: List<Drawn>,
)

/** Local storage. Cloud sync comes in a later version. */
class Store(context: Context) {
    private val p: SharedPreferences = context.getSharedPreferences("tarota", Context.MODE_PRIVATE)

    val journal = mutableStateListOf<Reading>()
    var onboarded by mutableStateOf(p.getBoolean("onboarded", false))
        private set
    var name by mutableStateOf(p.getString("name", "") ?: "")
    var birth by mutableStateOf(p.getString("birth", null)?.let { runCatching { LocalDate.parse(it) }.getOrNull() })
    var focus by mutableStateOf(p.getStringSet("focus", emptySet())?.toSet() ?: emptySet())
    var rel by mutableStateOf(p.getInt("rel", -1))
    var notif by mutableStateOf(p.getInt("notif", 0))
    var premium by mutableStateOf(p.getBoolean("premium", false))
        private set
    private val days = (p.getStringSet("days", emptySet()) ?: emptySet()).toMutableSet()

    init {
        p.getString("lang", null)?.let { if (it in LANGS) Lang.current = it }
        Th.mode = p.getInt("theme", 0)
        journal.addAll(loadJournal())
    }

    fun saveProfile() {
        p.edit()
            .putString("name", name.trim())
            .putString("birth", birth?.toString())
            .putStringSet("focus", focus)
            .putInt("rel", rel)
            .putInt("notif", notif)
            .apply()
    }

    fun finishOnboarding() {
        saveProfile()
        onboarded = true
        p.edit().putBoolean("onboarded", true).apply()
    }

    fun resetOnboarding() {
        onboarded = false
        p.edit().putBoolean("onboarded", false).apply()
    }

    /** Demo only: real Google Play Billing is wired in before premium goes live. */
    fun setPremiumDemo(v: Boolean) {
        premium = v
        p.edit().putBoolean("premium", v).apply()
    }

    var useReversed: Boolean
        get() = p.getBoolean("reversed", true)
        set(v) { p.edit().putBoolean("reversed", v).apply() }

    fun setLang(l: String) {
        Lang.current = l
        p.edit().putString("lang", l).apply()
    }

    fun setTheme(m: Int) {
        Th.mode = m
        p.edit().putInt("theme", m).apply()
    }

    fun markDay() {
        if (days.add(LocalDate.now().toString())) p.edit().putStringSet("days", days.toSet()).apply()
    }

    fun streak(): Int {
        var n = 0
        var d = LocalDate.now()
        while (d.toString() in days) { n++; d = d.minusDays(1) }
        return n
    }

    fun draw(n: Int): List<Drawn> =
        (0 until Deck.SIZE).shuffled().take(n).map { Drawn(it, useReversed && Random.nextInt(100) < 30) }

    /**
     * Personal card of the day: derived from name + birthday + today's date,
     * so the same person gets the same card on every device, and a new one tomorrow.
     */
    fun dailyCard(): Drawn {
        val today = LocalDate.now().toString()
        if (p.getString("daily_date", null) != today) {
            p.edit().putString("daily_date", today).putBoolean("daily_open", false).apply()
        }
        val seed = "${name.trim().lowercase()}|${birth}|$today".hashCode().toLong()
        val rnd = Random(seed)
        val card = rnd.nextInt(Deck.SIZE)
        val rev = useReversed && rnd.nextInt(100) < 30
        return Drawn(card, rev)
    }

    var dailyOpened: Boolean
        get() = p.getBoolean("daily_open", false) && p.getString("daily_date", null) == LocalDate.now().toString()
        set(v) { p.edit().putBoolean("daily_open", v).apply() }

    fun save(r: Reading) { journal.add(0, r); markDay(); persist() }
    fun delete(r: Reading) { journal.remove(r); persist() }
    fun clear() { journal.clear(); persist() }

    private fun persist() {
        val arr = JSONArray()
        journal.forEach { r ->
            val cards = JSONArray()
            r.cards.forEach { c -> cards.put(JSONObject().put("c", c.card).put("r", c.reversed)) }
            arr.put(JSONObject().put("t", r.time).put("s", r.spreadId).put("q", r.question).put("cards", cards))
        }
        p.edit().putString("journal", arr.toString()).apply()
    }

    private fun loadJournal(): List<Reading> = try {
        val arr = JSONArray(p.getString("journal", "[]"))
        (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            val cs = o.getJSONArray("cards")
            Reading(
                o.getLong("t"), o.getString("s"), o.optString("q", ""),
                (0 until cs.length()).map { j -> cs.getJSONObject(j).let { Drawn(it.getInt("c"), it.getBoolean("r")) } },
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}
