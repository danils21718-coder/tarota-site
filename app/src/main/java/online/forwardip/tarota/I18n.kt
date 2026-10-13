package online.forwardip.tarota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

val LANGS = listOf("ru", "kk", "en", "es")
val LANG_NAMES = mapOf("ru" to "Русский", "kk" to "Қазақша", "en" to "English", "es" to "Español")

object Lang {
    var current by mutableStateOf(defaultLang())

    fun defaultLang(): String {
        val sys = Locale.getDefault().language
        return if (sys in LANGS) sys else "en"
    }

    /** index in ru/kk/en/es tables */
    val i: Int get() = LANGS.indexOf(current).coerceAtLeast(0)
}

/** Translate: values in order ru, kk, en, es */
private fun t(ru: String, kk: String, en: String, es: String) = listOf(ru, kk, en, es)

private val STR: Map<String, List<String>> = mapOf(
    "tab_today" to t("Главная", "Басты", "Home", "Inicio"),
    "tab_spreads" to t("Расклады", "Болжаулар", "Spreads", "Tiradas"),
    "tab_cards" to t("Карты", "Карталар", "Cards", "Cartas"),
    "tab_journal" to t("Дневник", "Күнделік", "Journal", "Diario"),
    "notif_title" to t("Твоя карта дня готова", "Күн картаң дайын", "Your card of the day is ready", "Tu carta del día está lista"),
    "notif_body" to t("Открой, чтобы узнать совет на сегодня ✦", "Бүгінгі кеңесті білу үшін аш ✦", "Open to see today's advice ✦", "Ábrela para ver el consejo de hoy ✦"),
    "settings" to t("Настройки", "Баптаулар", "Settings", "Ajustes"),
    "card_of_day" to t("Карта дня", "Күн картасы", "Card of the day", "Carta del día"),
    "daily_hint" to t(
        "Карта дня не меняется до завтра. Подумай, как её совет связан с твоим днём.",
        "Күн картасы ертеңге дейін өзгермейді. Оның кеңесі күніңе қалай қатысты екенін ойлан.",
        "Your card stays the same until tomorrow. Think about how its advice fits your day.",
        "Tu carta no cambia hasta mañana. Piensa cómo su consejo encaja en tu día."
    ),
    "tap_reveal" to t("Нажми, чтобы открыть", "Ашу үшін түрт", "Tap to reveal", "Toca para revelar"),
    "reversed" to t("Перевёрнутая", "Төңкерілген", "Reversed", "Invertida"),
    "upright" to t("Прямое положение", "Тура қалып", "Upright", "Al derecho"),
    "question_hint" to t("Твой вопрос (необязательно)", "Сұрағың (міндетті емес)", "Your question (optional)", "Tu pregunta (opcional)"),
    "draw" to t("Вытянуть карты", "Карта тарту", "Draw cards", "Sacar cartas"),
    "again" to t("Новый расклад", "Жаңа болжау", "New reading", "Nueva tirada"),
    "saved" to t("Сохранено в дневник", "Күнделікке сақталды", "Saved to journal", "Guardado en el diario"),
    "journal_empty" to t(
        "Пока нет раскладов. Сделай первый во вкладке «Расклады».",
        "Әзірге болжау жоқ. Алғашқысын «Болжаулар» бөлімінде жаса.",
        "No readings yet. Make your first one in Spreads.",
        "Aún no hay lecturas. Haz la primera en Tiradas."
    ),
    "delete" to t("Удалить", "Жою", "Delete", "Eliminar"),
    "language" to t("Язык", "Тіл", "Language", "Idioma"),
    "use_reversed" to t("Перевёрнутые карты", "Төңкерілген карталар", "Reversed cards", "Cartas invertidas"),
    "use_reversed_hint" to t(
        "Если выключить, все карты выпадают в прямом положении.",
        "Өшірсең, барлық карта тура қалыпта түседі.",
        "When off, every card is drawn upright.",
        "Si lo desactivas, todas las cartas salen al derecho."
    ),
    "privacy" to t("Политика конфиденциальности", "Құпиялылық саясаты", "Privacy policy", "Política de privacidad"),
    "terms" to t("Условия использования", "Пайдалану шарттары", "Terms of use", "Términos de uso"),
    "support" to t("Поддержка", "Қолдау", "Support", "Soporte"),
    "clear_journal" to t("Очистить дневник", "Күнделікті тазалау", "Clear journal", "Borrar diario"),
    "disclaimer" to t(
        "Tarota — для развлечения и саморефлексии. Не является медицинской, юридической или финансовой консультацией.",
        "Tarota — ойын-сауық пен өзін-өзі тануға арналған. Медициналық, заңдық немесе қаржылық кеңес емес.",
        "Tarota is for entertainment and self-reflection. It is not medical, legal or financial advice.",
        "Tarota es para entretenimiento y autorreflexión. No es asesoramiento médico, legal ni financiero."
    ),
    "yes" to t("Да", "Иә", "Yes", "Sí"),
    "no" to t("Нет", "Жоқ", "No", "No"),
    "maybe" to t("Возможно", "Мүмкін", "Maybe", "Quizás"),
    "back" to t("Назад", "Артқа", "Back", "Atrás"),
    "major" to t("Старшие арканы", "Үлкен аркандар", "Major Arcana", "Arcanos mayores"),
    "minor" to t("Младшие арканы", "Кіші аркандар", "Minor Arcana", "Arcanos menores"),
    "cards_count" to t("карт", "карта", "cards", "cartas"),
    "ob_w_t" to t("Карты Таро, которые слышат тебя", "Сені еститін Таро карталары", "Tarot that listens to you", "Un tarot que te escucha"),
    "ob_w_s" to t("Личные расклады, карта дня и дневник. Без навязчивой рекламы.", "Жеке болжаулар, күн картасы және күнделік. Мазалайтын жарнамасыз.", "Personal readings, a daily card and a journal. No annoying ads.", "Lecturas personales, carta del día y diario. Sin anuncios molestos."),
    "start" to t("Начать", "Бастау", "Get started", "Empezar"),
    "next" to t("Далее", "Келесі", "Continue", "Continuar"),
    "skip" to t("Пропустить", "Өткізіп жіберу", "Skip", "Omitir"),
    "ob_name_t" to t("Как тебя зовут?", "Атың кім?", "What's your name?", "¿Cómo te llamas?"),
    "ob_name_s" to t("Карты будут обращаться к тебе по имени.", "Карталар саған атыңмен жүгінеді.", "The cards will speak to you by name.", "Las cartas te hablarán por tu nombre."),
    "name_ph" to t("Имя", "Атың", "Your name", "Tu nombre"),
    "ob_birth_t" to t("Когда твой день рождения?", "Туған күнің қашан?", "When is your birthday?", "¿Cuándo es tu cumpleaños?"),
    "ob_birth_s" to t("Определим твой знак и аркан судьбы.", "Белгің мен тағдыр арканын анықтаймыз.", "We'll find your sign and your birth card.", "Descubriremos tu signo y tu carta de nacimiento."),
    "your_sign" to t("Знак", "Белгі", "Sign", "Signo"),
    "birth_card" to t("Аркан судьбы", "Тағдыр арканы", "Birth card", "Carta de nacimiento"),
    "ob_focus_t" to t("Что тебя волнует сейчас?", "Қазір сені не мазалайды?", "What's on your mind?", "¿Qué te preocupa ahora?"),
    "ob_focus_s" to t("Можно выбрать несколько.", "Бірнешеуін таңдауға болады.", "Pick as many as you like.", "Puedes elegir varias."),
    "ob_rel_t" to t("Твой статус в отношениях", "Қарым-қатынастағы жағдайың", "Your relationship status", "Tu situación sentimental"),
    "ob_rel_s" to t("Так толкования будут точнее.", "Осылай түсіндірме нақтырақ болады.", "This makes readings more personal.", "Así las lecturas serán más personales."),
    "ob_notif_t" to t("Когда присылать карту дня?", "Күн картасын қашан жіберейік?", "When should we send your daily card?", "¿Cuándo te enviamos la carta del día?"),
    "ob_notif_s" to t("Одно уведомление в день. Отключается в настройках.", "Күніне бір хабарлама. Баптауларда өшіріледі.", "One notification a day. Turn it off any time.", "Una notificación al día. Desactívala cuando quieras."),
    "ob_build_t" to t("Тасуем колоду для", "Колоданы араластырудамыз:", "Shuffling the deck for", "Barajando para"),
    "b1" to t("Определяем твой знак", "Белгіңді анықтаймыз", "Finding your sign", "Calculando tu signo"),
    "b2" to t("Считаем аркан судьбы", "Тағдыр арканын есептеудеміз", "Calculating your birth card", "Calculando tu carta de nacimiento"),
    "b3" to t("Подбираем расклады под твои темы", "Тақырыптарыңа болжау таңдаудамыз", "Choosing spreads for your topics", "Eligiendo tiradas para tus temas"),
    "pw_t" to t("Открой Tarota Premium", "Tarota Premium-ды аш", "Unlock Tarota Premium", "Desbloquea Tarota Premium"),
    "pw1" to t("Безлимитные AI-толкования", "Шексіз AI-түсіндірмелер", "Unlimited AI interpretations", "Interpretaciones con IA ilimitadas"),
    "pw2" to t("Все расклады, включая Кельтский крест", "Барлық болжау, Кельт кресін қоса", "Every spread, including Celtic Cross", "Todas las tiradas, incluida la Cruz Celta"),
    "pw3" to t("Дневник в облаке на всех устройствах", "Бұлттағы күнделік барлық құрылғыда", "Cloud journal on all your devices", "Diario en la nube en todos tus dispositivos"),
    "pw4" to t("Никакой рекламы", "Ешқандай жарнама жоқ", "No ads, ever", "Sin anuncios"),
    "year" to t("Год", "Жыл", "Yearly", "Anual"),
    "q3" to t("3 месяца", "3 ай", "3 months", "3 meses"),
    "q3_p" to t("3 дня бесплатно, затем 6,99 $ за 3 месяца · 2,33 $/мес", "3 күн тегін, кейін 3 айға 6,99 $ · айына 2,33 $", "3 days free, then $6.99 per 3 months · $2.33/mo", "3 días gratis, luego 6,99 $ cada 3 meses · 2,33 $/mes"),
    "best_q3" to t("Выгодно −42%", "Тиімді −42%", "Best value −42%", "Mejor precio −42%"),
    "day" to t("День", "Күн", "Day", "Día"),
    "month_w" to t("Месяц", "Ай", "Month", "Mes"),
    "year_w" to t("Год", "Жыл", "Year", "Año"),
    "month" to t("Месяц", "Ай", "Monthly", "Mensual"),
    "year_p" to t("29,99 $ в год", "Жылына 29,99 $", "$29.99/year", "29,99 $/año"),
    "month_p" to t("3,99 $ в месяц", "Айына 3,99 $", "$3.99/month", "3,99 $/mes"),
    "best" to t("−58%", "−58%", "−58%", "−58%"),
    "pw_cta" to t("Попробовать бесплатно", "Тегін байқап көру", "Start free trial", "Probar gratis"),
    "pw_cta2" to t("Подписаться", "Жазылу", "Subscribe", "Suscribirse"),
    "pw_note" to t("Отмена в любой момент. Напомним за день до списания.", "Кез келген уақытта бас тартуға болады. Төлемнен бір күн бұрын ескертеміз.", "Cancel anytime. We'll remind you a day before you're charged.", "Cancela cuando quieras. Te avisaremos un día antes del cobro."),
    "pw_restore" to t("Восстановить покупки", "Сатып алуды қалпына келтіру", "Restore purchases", "Restaurar compras"),
    "hi_m" to t("Доброе утро", "Қайырлы таң", "Good morning", "Buenos días"),
    "hi_d" to t("Добрый день", "Қайырлы күн", "Good afternoon", "Buenas tardes"),
    "hi_e" to t("Добрый вечер", "Қайырлы кеш", "Good evening", "Buenas noches"),
    "ask_t" to t("Задай вопрос картам", "Карталарға сұрақ қой", "Ask the cards", "Pregunta a las cartas"),
    "ask_ph" to t("Например: что меня ждёт на этой неделе?", "Мысалы: осы аптада мені не күтіп тұр?", "e.g. What awaits me this week?", "Ej.: ¿Qué me espera esta semana?"),
    "ask_btn" to t("Вытянуть 3 карты", "3 карта тарту", "Draw 3 cards", "Sacar 3 cartas"),
    "quick" to t("Быстрые расклады", "Жылдам болжаулар", "Quick spreads", "Tiradas rápidas"),
    "streak" to t("дн. подряд", "күн қатарынан", "day streak", "días seguidos"),
    "daily_open" to t("Открыть карту", "Картаны ашу", "Reveal card", "Revelar carta"),
    "all_spreads" to t("Все расклады", "Барлық болжаулар", "All spreads", "Todas las tiradas"),
    "profile" to t("Профиль", "Профиль", "Profile", "Perfil"),
    "redo" to t("Пройти знакомство заново", "Танысуды қайта өту", "Redo onboarding", "Repetir la bienvenida"),
    "premium" to t("Tarota Premium", "Tarota Premium", "Tarota Premium", "Tarota Premium"),
    "locked" to t("Premium", "Premium", "Premium", "Premium"),
    "tab_learn" to t("Обучение", "Оқу", "Learn", "Aprende"),
    "lessons" to t("Уроки", "Сабақтар", "Lessons", "Lecciones"),
    "lesson" to t("Урок", "Сабақ", "Lesson", "Lección"),
    "practice" to t("Тренировка", "Жаттығу", "Practice", "Práctica"),
    "quiz_t" to t("Угадай значение карты", "Карта мағынасын тап", "Guess the card's meaning", "Adivina el significado"),
    "quiz_s" to t("Карта и 3 варианта. Запоминай значения в игре.", "Карта және 3 нұсқа. Мағыналарды ойнап жатта.", "One card, three options. Learn meanings by playing.", "Una carta y tres opciones. Aprende jugando."),
    "quiz_q" to t("Что означает эта карта?", "Бұл карта нені білдіреді?", "What does this card mean?", "¿Qué significa esta carta?"),
    "correct" to t("Верно!", "Дұрыс!", "Correct!", "¡Correcto!"),
    "wrong" to t("Не совсем", "Дұрыс емес", "Not quite", "No del todo"),
    "score" to t("Счёт", "Ұпай", "Score", "Puntos"),
    "next_card" to t("Следующая карта", "Келесі карта", "Next card", "Siguiente carta"),
    "all_cards" to t("Все 78 карт", "Барлық 78 карта", "All 78 cards", "Las 78 cartas"),
    "theme" to t("Тема", "Тақырып", "Theme", "Tema"),
    "theme_sys" to t("Как в системе", "Жүйедегідей", "System", "Sistema"),
    "theme_light" to t("Дневная", "Күндізгі", "Light", "Clara"),
    "theme_dark" to t("Тёмная", "Қараңғы", "Dark", "Oscura"),
    "trip_sum" to t("Общий ответ трёх карт", "Үш картаның ортақ жауабы", "What the three cards say together", "Lo que dicen las tres cartas juntas"),
    "choose_spread" to t("Выбери расклад", "Болжауды таңда", "Choose a spread", "Elige una tirada"),
)

fun tr(key: String): String = STR[key]?.get(Lang.i) ?: key
