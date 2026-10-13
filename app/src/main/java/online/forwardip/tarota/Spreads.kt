package online.forwardip.tarota

/** Names and position labels ordered ru, kk, en, es. */
data class Spread(
    val id: String,
    val icon: String,
    val color: Long,
    val pro: Boolean,
    val names: List<String>,
    val positions: List<List<String>>,
) {
    val size: Int get() = positions[0].size
    fun title() = names[Lang.i]
    fun position(i: Int) = positions[Lang.i][i]
}

object Spreads {
    val all = listOf(
        Spread(
            "yesno", "yes", 0xFFF3E1A8, false,
            listOf("Да или нет", "Иә немесе жоқ", "Yes or No", "Sí o No"),
            listOf(listOf("Ответ"), listOf("Жауап"), listOf("Answer"), listOf("Respuesta")),
        ),
        Spread(
            "triplet", "spreads", 0xFFE6C9A8, false,
            listOf("Триплет", "Триплет", "Triplet", "Triplete"),
            listOf(
                listOf("Карта 1", "Карта 2", "Карта 3"),
                listOf("1-карта", "2-карта", "3-карта"),
                listOf("Card 1", "Card 2", "Card 3"),
                listOf("Carta 1", "Carta 2", "Carta 3"),
            ),
        ),
        Spread(
            "love", "heart", 0xFFF2C9B4, false,
            listOf("На любовь", "Махаббат", "Love", "Amor"),
            listOf(
                listOf("Ты", "Партнёр", "Ваша связь"),
                listOf("Сен", "Серігің", "Араларыңдағы байланыс"),
                listOf("You", "Partner", "Your connection"),
                listOf("Tú", "Tu pareja", "Vuestra conexión"),
            ),
        ),
        Spread(
            "three", "moon", 0xFFE3D6F0, false,
            listOf("Прошлое · Настоящее · Будущее", "Өткен · Қазір · Болашақ", "Past · Present · Future", "Pasado · Presente · Futuro"),
            listOf(
                listOf("Прошлое", "Настоящее", "Будущее"),
                listOf("Өткен", "Қазір", "Болашақ"),
                listOf("Past", "Present", "Future"),
                listOf("Pasado", "Presente", "Futuro"),
            ),
        ),
        Spread(
            "career", "leaf", 0xFFCFDCCB, false,
            listOf("На работу и деньги", "Жұмыс пен ақша", "Work & money", "Trabajo y dinero"),
            listOf(
                listOf("Ситуация", "Препятствие", "Совет"),
                listOf("Жағдай", "Кедергі", "Кеңес"),
                listOf("Situation", "Obstacle", "Advice"),
                listOf("Situación", "Obstáculo", "Consejo"),
            ),
        ),
        Spread(
            "celtic", "cross", 0xFF1F1B33, true,
            listOf("Кельтский крест", "Кельт кресі", "Celtic Cross", "Cruz Celta"),
            listOf(
                listOf("Настоящее", "Препятствие", "Прошлое", "Ближайшее будущее", "Сознательная цель", "Скрытое", "Совет", "Окружение", "Надежды и страхи", "Итог"),
                listOf("Қазіргі жағдай", "Кедергі", "Өткен", "Жақын болашақ", "Саналы мақсат", "Жасырын", "Кеңес", "Айналаң", "Үміт пен қорқыныш", "Нәтиже"),
                listOf("Present", "Challenge", "Past", "Near future", "Conscious goal", "Hidden", "Advice", "Surroundings", "Hopes and fears", "Outcome"),
                listOf("Presente", "Desafío", "Pasado", "Futuro cercano", "Meta consciente", "Lo oculto", "Consejo", "Entorno", "Esperanzas y miedos", "Resultado"),
            ),
        ),
    )

    fun byId(id: String) = all.firstOrNull { it.id == id }
}
