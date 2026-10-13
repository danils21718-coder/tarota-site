package online.forwardip.tarota

import java.time.LocalDate

/** Lists ordered ru, kk, en, es. */
object Profile {
    val FOCUS = listOf(
        "love" to listOf("Любовь", "Махаббат", "Love", "Amor"),
        "career" to listOf("Карьера", "Мансап", "Career", "Carrera"),
        "money" to listOf("Деньги", "Ақша", "Money", "Dinero"),
        "growth" to listOf("Саморазвитие", "Өзін дамыту", "Self-growth", "Crecimiento"),
        "family" to listOf("Семья", "Отбасы", "Family", "Familia"),
        "health" to listOf("Здоровье", "Денсаулық", "Health", "Salud"),
    )
    val REL = listOf(
        listOf("Свободен(а)", "Бос", "Single", "Soltero/a"),
        listOf("Влюблён(а)", "Ғашықпын", "In love", "Enamorado/a"),
        listOf("В отношениях", "Қарым-қатынастамын", "In a relationship", "En pareja"),
        listOf("В браке", "Отбасылымын", "Married", "Casado/a"),
        listOf("Всё сложно", "Бәрі күрделі", "It's complicated", "Es complicado"),
    )
    /** hour of day, or -1 = no notifications */
    val NOTIF_HOURS = listOf(8, 13, 20, -1)
    val NOTIF = listOf(
        listOf("Утром · 8:00", "Таңертең · 8:00", "Morning · 8:00", "Mañana · 8:00"),
        listOf("Днём · 13:00", "Түсте · 13:00", "Midday · 13:00", "Mediodía · 13:00"),
        listOf("Вечером · 20:00", "Кешке · 20:00", "Evening · 20:00", "Noche · 20:00"),
        listOf("Не присылать", "Жібермеу", "Don't send", "No enviar"),
    )
    private val ZOD = listOf(
        listOf("Козерог", "Тауешкі", "Capricorn", "Capricornio"),
        listOf("Водолей", "Суқұйғыш", "Aquarius", "Acuario"),
        listOf("Рыбы", "Балықтар", "Pisces", "Piscis"),
        listOf("Овен", "Тоқты", "Aries", "Aries"),
        listOf("Телец", "Торпақ", "Taurus", "Tauro"),
        listOf("Близнецы", "Егіздер", "Gemini", "Géminis"),
        listOf("Рак", "Шаян", "Cancer", "Cáncer"),
        listOf("Лев", "Арыстан", "Leo", "Leo"),
        listOf("Дева", "Бикеш", "Virgo", "Virgo"),
        listOf("Весы", "Таразы", "Libra", "Libra"),
        listOf("Скорпион", "Сарышаян", "Scorpio", "Escorpio"),
        listOf("Стрелец", "Мерген", "Sagittarius", "Sagitario"),
    )

    fun zodiac(date: LocalDate): String {
        val cut = intArrayOf(20, 19, 21, 20, 21, 21, 23, 23, 23, 23, 22, 22)
        var i = date.monthValue - 1
        if (date.dayOfMonth < cut[date.monthValue - 1]) i = (i + 11) % 12
        return ZOD[(i + 1) % 12][Lang.i]
    }

    /** Birth card: digit sum of yyyyMMdd reduced to 0..21. */
    fun birthCard(date: LocalDate): Int {
        var n = date.toString().filter { it.isDigit() }.sumOf { it - '0' }
        while (n > 21) n = n.toString().sumOf { it - '0' }
        return n
    }
}
