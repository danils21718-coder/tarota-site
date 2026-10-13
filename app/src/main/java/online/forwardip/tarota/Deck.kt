package online.forwardip.tarota

/**
 * 78-card Rider–Waite structure.
 * id 0..21  = Major Arcana
 * id 22..77 = Minor Arcana: suit = (id-22)/14 (0 Wands, 1 Cups, 2 Swords, 3 Pentacles), rank = (id-22)%14 (0 Ace .. 13 King)
 * All tables are ordered ru, kk, en, es.
 */
object Deck {
    const val SIZE = 78

    fun isMajor(id: Int) = id < 22
    fun suit(id: Int) = (id - 22) / 14
    fun rank(id: Int) = (id - 22) % 14

    // ---------------- MAJOR NAMES ----------------
    private val MAJOR_NAMES = listOf(
        listOf("Шут", "Маг", "Верховная Жрица", "Императрица", "Император", "Иерофант", "Влюблённые", "Колесница", "Сила", "Отшельник", "Колесо Фортуны", "Справедливость", "Повешенный", "Смерть", "Умеренность", "Дьявол", "Башня", "Звезда", "Луна", "Солнце", "Суд", "Мир"),
        listOf("Сайқымазақ", "Сиқыршы", "Бас абыз әйел", "Императрица", "Император", "Иерофант", "Ғашықтар", "Арба", "Күш", "Дәруіш", "Тағдыр дөңгелегі", "Әділдік", "Асылған адам", "Өлім", "Ұстамдылық", "Шайтан", "Мұнара", "Жұлдыз", "Ай", "Күн", "Сот", "Әлем"),
        listOf("The Fool", "The Magician", "The High Priestess", "The Empress", "The Emperor", "The Hierophant", "The Lovers", "The Chariot", "Strength", "The Hermit", "Wheel of Fortune", "Justice", "The Hanged Man", "Death", "Temperance", "The Devil", "The Tower", "The Star", "The Moon", "The Sun", "Judgement", "The World"),
        listOf("El Loco", "El Mago", "La Sacerdotisa", "La Emperatriz", "El Emperador", "El Hierofante", "Los Enamorados", "El Carro", "La Fuerza", "El Ermitaño", "La Rueda de la Fortuna", "La Justicia", "El Colgado", "La Muerte", "La Templanza", "El Diablo", "La Torre", "La Estrella", "La Luna", "El Sol", "El Juicio", "El Mundo"),
    )

    // ---------------- MAJOR MEANINGS: upright ----------------
    private val MAJOR_UP = listOf(
        listOf(
            "Новое начало, спонтанность и доверие к пути.",
            "Сила воли, умения и все ресурсы, чтобы задуманное сбылось.",
            "Интуиция, внутреннее знание и тихая мудрость.",
            "Изобилие, забота, творчество и рост.",
            "Структура, стабильность и ясное руководство.",
            "Традиции, обучение и надёжный наставник.",
            "Любовь, гармония и важный выбор.",
            "Решимость, движение вперёд и победа через фокус.",
            "Внутренняя сила, терпение и мягкая смелость.",
            "Размышление, уединение и поиск истины.",
            "Перемены, циклы и поворот удачи.",
            "Честность, истина и закон причины и следствия.",
            "Пауза, принятие и новый взгляд на ситуацию.",
            "Завершение, которое освобождает место для перемен.",
            "Баланс, терпение и умеренность.",
            "Привязанности, соблазны и шаблоны, которые стоит заметить.",
            "Внезапная перемена, которая расчищает путь к правде.",
            "Надежда, исцеление и новая вера в себя.",
            "Интуиция, сны и неопределённость.",
            "Радость, успех и тепло.",
            "Пробуждение, переосмысление и новое призвание.",
            "Завершённость, целостность и достижение.",
        ),
        listOf(
            "Жаңа бастама, еркіндік және жолға сенім.",
            "Ерік-жігер, шеберлік және ойыңды жүзеге асыратын ресурстар.",
            "Түйсік, ішкі білім және сабырлы даналық.",
            "Молшылық, қамқорлық, шығармашылық және өсу.",
            "Тәртіп, тұрақтылық және айқын басшылық.",
            "Дәстүр, оқу және сенімді ұстаз.",
            "Махаббат, үйлесім және маңызды таңдау.",
            "Батылдық, алға қозғалыс және назар арқылы жеңіс.",
            "Ішкі күш, сабыр және жұмсақ батылдық.",
            "Ой толғау, оңашалық және ақиқатты іздеу.",
            "Өзгеріс, кезеңдер және сәттіліктің бұрылысы.",
            "Әділдік, ақиқат және себеп пен салдар.",
            "Үзіліс, қабылдау және жағдайға жаңа көзқарас.",
            "Жаңа өзгеріске орын ашатын аяқталу.",
            "Тепе-теңдік, сабыр және байсалдылық.",
            "Тәуелділік, азғыру және байқауға тұрарлық әдеттер.",
            "Ақиқатқа жол ашатын кенет өзгеріс.",
            "Үміт, сауығу және өзіңе жаңа сенім.",
            "Түйсік, түстер және белгісіздік.",
            "Қуаныш, табыс және жылулық.",
            "Ояну, қайта ой елегінен өткізу және жаңа шақыру.",
            "Аяқталу, тұтастық және жетістік.",
        ),
        listOf(
            "New beginnings, spontaneity and trust in the journey.",
            "Willpower, skill and the resources to make it happen.",
            "Intuition, inner knowing and quiet wisdom.",
            "Abundance, care, creativity and growth.",
            "Structure, stability and clear leadership.",
            "Tradition, learning and trusted guidance.",
            "Love, harmony and a meaningful choice.",
            "Determination, momentum and victory through focus.",
            "Inner strength, patience and gentle courage.",
            "Reflection, solitude and the search for truth.",
            "Change, cycles and a turn of luck.",
            "Fairness, truth and cause and effect.",
            "A pause, surrender and a new perspective.",
            "An ending that makes space for transformation.",
            "Balance, patience and moderation.",
            "Attachments, temptation and patterns worth noticing.",
            "Sudden change that clears the way for truth.",
            "Hope, healing and renewed faith in yourself.",
            "Intuition, dreams and uncertainty.",
            "Joy, success and warmth.",
            "Awakening, reflection and a fresh calling.",
            "Completion, wholeness and achievement.",
        ),
        listOf(
            "Nuevos comienzos, espontaneidad y confianza en el camino.",
            "Voluntad, habilidad y los recursos para lograrlo.",
            "Intuición, sabiduría interior y calma.",
            "Abundancia, cuidado, creatividad y crecimiento.",
            "Estructura, estabilidad y liderazgo claro.",
            "Tradición, aprendizaje y una guía de confianza.",
            "Amor, armonía y una elección importante.",
            "Determinación, impulso y victoria gracias al enfoque.",
            "Fuerza interior, paciencia y valentía serena.",
            "Reflexión, soledad y búsqueda de la verdad.",
            "Cambio, ciclos y un giro de la suerte.",
            "Equidad, verdad y causa y efecto.",
            "Una pausa, aceptación y una nueva perspectiva.",
            "Un final que abre espacio a la transformación.",
            "Equilibrio, paciencia y moderación.",
            "Apegos, tentaciones y patrones que conviene observar.",
            "Un cambio repentino que despeja el camino hacia la verdad.",
            "Esperanza, sanación y nueva fe en ti.",
            "Intuición, sueños e incertidumbre.",
            "Alegría, éxito y calidez.",
            "Despertar, reflexión y una nueva vocación.",
            "Plenitud, integridad y logro.",
        ),
    )

    // ---------------- MAJOR MEANINGS: reversed ----------------
    private val MAJOR_REV = listOf(
        listOf(
            "Сомнения или безрассудство — сначала осмотрись.",
            "Распылённая энергия или неиспользованный талант — сфокусируйся.",
            "Ты не слышишь внутренний голос — замедлись и прислушайся.",
            "Творческий застой или ты отдаёшь слишком много — позаботься и о себе.",
            "Жёсткость или контроль — добавь гибкости.",
            "Сомнение в правилах и поиск своего пути.",
            "Неуверенность в выборе — вернись к своим ценностям.",
            "Потеря направления — собери силы, прежде чем двигаться.",
            "Неуверенность в себе — будь к себе добрее и восстанавливай силы.",
            "Изоляция — возможно, пора снова выйти к людям.",
            "Сопротивление переменам — чему учит этот цикл?",
            "Несправедливость или уход от ответственности — будь честен с собой.",
            "Застой — маленький шаг поможет сдвинуться.",
            "Держишься за прошлое — отпустив, почувствуешь облегчение.",
            "Перегиб или спешка — вернись к спокойному ритму.",
            "Освобождение и возвращение своей силы.",
            "Ты избегаешь нужной перемены — или буря уже проходит.",
            "Уныние — маленькие шаги вернут свет.",
            "Туман рассеивается — правда выходит наружу.",
            "Временные тучи — оптимизм вернётся.",
            "Самокритика — прости себя и иди дальше.",
            "Почти у цели — заверши начатое.",
        ),
        listOf(
            "Күмән немесе асығыстық — алдымен айналаңа қара.",
            "Шашыраған қуат немесе пайдаланылмаған қабілет — назарыңды жина.",
            "Ішкі даусыңды естімей жүрсің — баяула да, тыңда.",
            "Шығармашылық тоқырау немесе тым көп беру — өзіңе де қамқор бол.",
            "Қатқылдық не бақылау — икемділік қос.",
            "Ережеге күмән және өз жолыңды іздеу.",
            "Таңдаудағы күмән — өз құндылықтарыңа орал.",
            "Бағытты жоғалту — қозғалмас бұрын күш жина.",
            "Өзіңе сенбеу — өзіңе мейірімді бол, күшіңді қалпына келтір.",
            "Оқшаулану — адамдармен қайта байланысатын уақыт болар.",
            "Өзгеріске қарсылық — бұл кезең неге үйретеді?",
            "Әділетсіздік не жауапкершіліктен қашу — өзіңе адал бол.",
            "Тоқырау — кішкентай қадам алға жылжытады.",
            "Өткенге жабысу — жіберсең, жеңілдік табасың.",
            "Асқыну не асығу — сабырлы ырғаққа орал.",
            "Босану және өз күшіңді қайтару.",
            "Қажетті өзгерістен қашу — немесе дауыл өтіп барады.",
            "Көңіл түсу — кішкентай қадамдар жарықты қайтарады.",
            "Тұман сейіледі — ақиқат ашылады.",
            "Уақытша бұлт — үмітің қайта оралады.",
            "Өзіңді сынау — өзіңді кешір де, алға жүр.",
            "Мақсатқа жақынсың — бастағаныңды аяқта.",
        ),
        listOf(
            "Hesitation or recklessness — look before you leap.",
            "Scattered energy or unused talent — focus your intention.",
            "Ignoring your inner voice — slow down and listen.",
            "Creative block or giving too much — care for yourself too.",
            "Rigidity or control — balance order with flexibility.",
            "Questioning the rules and finding your own path.",
            "Doubt about a choice — reconnect with your values.",
            "Losing direction — gather your energy before moving on.",
            "Self-doubt — be kind to yourself and rebuild confidence.",
            "Isolation — it may be time to reconnect with others.",
            "Resisting change — what can this cycle teach you?",
            "Unfairness or avoided responsibility — be honest with yourself.",
            "Feeling stuck — one small step can release it.",
            "Holding on to the past — letting go brings relief.",
            "Excess or haste — return to a steady rhythm.",
            "Breaking free and reclaiming your power.",
            "Avoiding a needed change — or the storm is passing.",
            "Discouragement — small acts of hope restore the light.",
            "Confusion clearing — the truth comes to light.",
            "Temporary clouds — your optimism will return.",
            "Self-criticism — forgive yourself and move forward.",
            "Almost there — finish what you started.",
        ),
        listOf(
            "Duda o imprudencia: mira antes de saltar.",
            "Energía dispersa o talento sin usar: enfoca tu intención.",
            "Ignoras tu voz interior: baja el ritmo y escucha.",
            "Bloqueo creativo o das demasiado: cuídate también a ti.",
            "Rigidez o control: equilibra el orden con flexibilidad.",
            "Cuestionar las reglas y buscar tu propio camino.",
            "Dudas ante una elección: vuelve a tus valores.",
            "Pérdida de rumbo: reúne fuerzas antes de avanzar.",
            "Inseguridad: sé amable contigo y recupera la confianza.",
            "Aislamiento: quizá es momento de reconectar con otros.",
            "Resistencia al cambio: ¿qué te enseña este ciclo?",
            "Injusticia o responsabilidad evitada: sé honesto contigo.",
            "Estancamiento: un pequeño paso puede liberarte.",
            "Aferrarte al pasado: soltar trae alivio.",
            "Exceso o prisa: vuelve a un ritmo sereno.",
            "Liberarte y recuperar tu poder.",
            "Evitas un cambio necesario, o la tormenta ya pasa.",
            "Desánimo: pequeños gestos de esperanza devuelven la luz.",
            "La confusión se disipa: la verdad sale a la luz.",
            "Nubes pasajeras: tu optimismo volverá.",
            "Autocrítica: perdónate y sigue adelante.",
            "Casi lo logras: termina lo que empezaste.",
        ),
    )

    // ---------------- MINOR NAMES ----------------
    private val SUIT_NAMES = listOf(
        listOf("Жезлов", "Кубков", "Мечей", "Пентаклей"),          // ru (genitive)
        listOf("Таяқтар", "Тостағандар", "Қылыштар", "Пентакльдер"), // kk
        listOf("Wands", "Cups", "Swords", "Pentacles"),             // en
        listOf("Bastos", "Copas", "Espadas", "Oros"),               // es
    )
    private val RANK_NAMES = listOf(
        listOf("Туз", "Двойка", "Тройка", "Четвёрка", "Пятёрка", "Шестёрка", "Семёрка", "Восьмёрка", "Девятка", "Десятка", "Паж", "Рыцарь", "Королева", "Король"),
        listOf("тузы", "екісі", "үші", "төрті", "бесі", "алтысы", "жетісі", "сегізі", "тоғызы", "оны", "пажы", "рыцары", "патшайымы", "патшасы"),
        listOf("Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Page", "Knight", "Queen", "King"),
        listOf("As", "Dos", "Tres", "Cuatro", "Cinco", "Seis", "Siete", "Ocho", "Nueve", "Diez", "Sota", "Caballero", "Reina", "Rey"),
    )

    // ---------------- MINOR MEANINGS: rank × suit ----------------
    private val RANK_UP = listOf(
        listOf("Новое начало и свежие возможности", "Баланс, партнёрство и выбор", "Рост, сотрудничество и первые результаты", "Стабильность, отдых и закрепление", "Испытание, конфликт или потеря", "Гармония, поддержка и прогресс", "Оценка, стратегия и упорство", "Движение, усилия и мастерство", "Почти у цели: стойкость и удовлетворение", "Завершение цикла: полнота или груз", "Любопытство, новости и свежий старт", "Действие, напор и стремление", "Забота, зрелость и внутренняя уверенность", "Мастерство, лидерство и ответственность"),
        listOf("Жаңа бастама мен тың мүмкіндік", "Тепе-теңдік, серіктестік және таңдау", "Өсу, ынтымақ және алғашқы нәтиже", "Тұрақтылық, демалыс және бекіту", "Сынақ, қақтығыс немесе жоғалту", "Үйлесім, қолдау және ілгерілеу", "Бағалау, стратегия және табандылық", "Қозғалыс, еңбек және шеберлік", "Мақсатқа жақын: төзім мен қанағат", "Кезеңнің аяқталуы: толықтық не жүк", "Қызығушылық, жаңалық және жаңа бастау", "Әрекет, екпін және ұмтылыс", "Қамқорлық, кемелдік және ішкі сенім", "Шеберлік, көшбасшылық және жауапкершілік"),
        listOf("A new beginning and fresh potential", "Balance, partnership and a choice", "Growth, collaboration and first results", "Stability, rest and consolidation", "Challenge, conflict or loss", "Harmony, support and progress", "Assessment, strategy and perseverance", "Movement, effort and mastery", "Near completion: resilience and fulfilment", "End of a cycle: fullness or burden", "Curiosity, news and a fresh start", "Action, drive and pursuit", "Nurturing, maturity and inner confidence", "Mastery, leadership and responsibility"),
        listOf("Un nuevo comienzo y potencial fresco", "Equilibrio, asociación y una elección", "Crecimiento, colaboración y primeros resultados", "Estabilidad, descanso y consolidación", "Desafío, conflicto o pérdida", "Armonía, apoyo y progreso", "Evaluación, estrategia y perseverancia", "Movimiento, esfuerzo y maestría", "Casi al final: resiliencia y satisfacción", "Fin de un ciclo: plenitud o carga", "Curiosidad, noticias y un nuevo inicio", "Acción, impulso y búsqueda", "Cuidado, madurez y confianza interior", "Maestría, liderazgo y responsabilidad"),
    )
    private val RANK_REV = listOf(
        listOf("Задержка или упущенная возможность", "Дисбаланс или нерешительность", "Рост тормозит или не хватает командной работы", "Застой или сопротивление переменам", "Восстановление и выход из трудностей", "Перекос между «давать» и «брать»", "Сомнения, отвлечение или нет фокуса", "Замедление, раздражение, чувство застоя", "Тревога или усталость перед финишем", "Освобождение от груза или незавершённый цикл", "Незрелость или неясные сигналы", "Спешка или распылённая энергия", "Неуверенность или чрезмерная отдача", "Проблемы с контролем или злоупотребление властью"),
        listOf("Кешігу немесе жіберілген мүмкіндік", "Тепе-теңдіктің бұзылуы не шешімсіздік", "Өсу баяулайды не ынтымақ жетіспейді", "Тоқырау не өзгеріске қарсылық", "Қалпына келу және қиындықтан шығу", "Беру мен алу арасындағы теңсіздік", "Күмән, алаңдау не назардың болмауы", "Баяулау, ашу, тұйыққа тірелу сезімі", "Мәре алдындағы алаң не шаршау", "Жүктен босану не аяқталмаған кезең", "Пісіп-жетілмеу не түсініксіз белгілер", "Асығыстық не шашыраған қуат", "Сенімсіздік не тым көп беру", "Бақылау мәселесі не билікті теріс пайдалану"),
        listOf("Delays or a missed opportunity", "Imbalance or indecision", "Slow growth or weak teamwork", "Stagnation or resistance to change", "Recovery and moving past difficulty", "Imbalance between giving and taking", "Doubt, distraction or lack of focus", "Slowdown, frustration or feeling stuck", "Worry or fatigue before the finish", "Releasing a burden or an unfinished cycle", "Immaturity or unclear messages", "Haste or scattered energy", "Insecurity or overgiving", "Control issues or misuse of power"),
        listOf("Retrasos o una oportunidad perdida", "Desequilibrio o indecisión", "Crecimiento lento o poco trabajo en equipo", "Estancamiento o resistencia al cambio", "Recuperación y superación de dificultades", "Desequilibrio entre dar y recibir", "Duda, distracción o falta de enfoque", "Lentitud, frustración o sensación de bloqueo", "Preocupación o cansancio antes de la meta", "Liberarte de una carga o un ciclo sin cerrar", "Inmadurez o mensajes poco claros", "Prisa o energía dispersa", "Inseguridad o dar demasiado", "Problemas de control o abuso de poder"),
    )
    private val SUIT_DOMAIN = listOf(
        listOf("в страсти, творчестве и делах", "в чувствах и отношениях", "в мыслях, решениях и общении", "в деньгах, работе и здоровье"),
        listOf("құштарлықта, шығармашылықта және істе", "сезімде және қарым-қатынаста", "ойда, шешімде және қарым-қатынаста", "ақшада, жұмыста және денсаулықта"),
        listOf("in passion, creativity and action", "in feelings and relationships", "in thoughts, decisions and communication", "in money, work and health"),
        listOf("en la pasión, la creatividad y la acción", "en los sentimientos y las relaciones", "en los pensamientos, las decisiones y la comunicación", "en el dinero, el trabajo y la salud"),
    )

    // ---------------- API ----------------
    fun name(id: Int): String {
        val l = Lang.i
        if (isMajor(id)) return MAJOR_NAMES[l][id]
        val s = SUIT_NAMES[l][suit(id)]
        val r = RANK_NAMES[l][rank(id)]
        return when (Lang.current) {
            "ru" -> "$r $s"
            "kk" -> "$s $r"
            "en" -> "$r of $s"
            else -> "$r de $s"
        }
    }

    fun meaning(id: Int, reversed: Boolean): String {
        val l = Lang.i
        if (isMajor(id)) return if (reversed) MAJOR_REV[l][id] else MAJOR_UP[l][id]
        val base = if (reversed) RANK_REV[l][rank(id)] else RANK_UP[l][rank(id)]
        return "$base — ${SUIT_DOMAIN[l][suit(id)]}."
    }

    private val ROMAN = listOf("0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI")
    private val SUIT_GLYPH = listOf("🔥", "💧", "⚔", "🌿")
    private val RANK_SHORT = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "P", "Kn", "Q", "K")

    fun glyph(id: Int) = if (isMajor(id)) "✦" else SUIT_GLYPH[suit(id)]
    fun label(id: Int) = if (isMajor(id)) ROMAN[id] else RANK_SHORT[rank(id)]

    /** +1 positive, -1 challenging, 0 neutral (used for Yes/No). */
    fun polarity(id: Int): Int {
        if (isMajor(id)) return when (id) {
            13, 15, 16 -> -1
            10, 12, 18 -> 0
            else -> 1
        }
        val r = rank(id); val s = suit(id)
        if (r == 4) return -1
        if (s == 2 && r in listOf(2, 7, 8, 9)) return -1
        if (r == 1 || r == 6) return 0
        return 1
    }

    // ---------------- TRIPLET: combined reading ----------------
    private fun t4(ru: String, kk: String, en: String, es: String) = listOf(ru, kk, en, es)
    private val TONE = listOf(
        t4("Карты складываются в благоприятную картину: ситуация развивается в твою пользу.",
           "Карталар жағымды көрініс береді: жағдай сенің пайдаңа дамып келеді.",
           "Together the cards paint a favourable picture: things are moving in your favour.",
           "Juntas, las cartas muestran un panorama favorable: las cosas avanzan a tu favor."),
        t4("Картина смешанная: есть и поддержка, и препятствия — многое зависит от твоих шагов.",
           "Көрініс аралас: қолдау да, кедергі де бар — көп нәрсе өз қадамдарыңа байланысты.",
           "The picture is mixed: there is support and there are obstacles — a lot depends on your next steps.",
           "El panorama es mixto: hay apoyo y también obstáculos; mucho depende de tus próximos pasos."),
        t4("Карты предупреждают: сейчас непростой период, стоит действовать осторожно и не спешить.",
           "Карталар ескертеді: қазір оңай кезең емес, абай болып, асықпаған жөн.",
           "The cards urge caution: this is a demanding period, so move carefully and don't rush.",
           "Las cartas aconsejan prudencia: es un periodo exigente, avanza con cuidado y sin prisas."),
    )
    private val FOCUS = listOf(
        t4("Главная тема — действие, энергия и личные цели.", "Басты тақырып — әрекет, қуат және жеке мақсаттар.", "The main theme is action, energy and personal goals.", "El tema principal es la acción, la energía y tus metas."),
        t4("Главная тема — чувства и отношения.", "Басты тақырып — сезім және қарым-қатынас.", "The main theme is feelings and relationships.", "El tema principal son los sentimientos y las relaciones."),
        t4("Главная тема — мысли, решения и разговоры.", "Басты тақырып — ой, шешім және әңгіме.", "The main theme is thoughts, decisions and conversations.", "El tema principal son los pensamientos, decisiones y conversaciones."),
        t4("Главная тема — деньги, работа и практические дела.", "Басты тақырып — ақша, жұмыс және практикалық істер.", "The main theme is money, work and practical matters.", "El tema principal es el dinero, el trabajo y lo práctico."),
    )
    private val MAJORS = t4(
        "Несколько Старших арканов: это важный, судьбоносный момент.",
        "Бірнеше Үлкен аркан: бұл маңызды, тағдыршешті сәт.",
        "Several Major Arcana: this is an important, pivotal moment.",
        "Varios Arcanos Mayores: es un momento importante y decisivo.",
    )
    private val KEY = t4("Ключевая карта — ", "Негізгі карта — ", "Key card: ", "Carta clave: ")

    fun tripletSummary(cards: List<Drawn>): String {
        val l = Lang.i
        val score = cards.sumOf { polarity(it.card) * if (it.reversed) -1 else 1 }
        val parts = mutableListOf(TONE[if (score >= 2) 0 else if (score <= -1) 2 else 1][l])
        val majors = cards.count { isMajor(it.card) }
        if (majors >= 2) parts += MAJORS[l]
        val suits = cards.filter { !isMajor(it.card) }.groupingBy { suit(it.card) }.eachCount()
        suits.maxByOrNull { it.value }?.takeIf { it.value >= 2 }?.let { parts += FOCUS[it.key][l] }
        val key = cards.firstOrNull { isMajor(it.card) } ?: cards[1]
        parts += KEY[l] + name(key.card) + ": " + meaning(key.card, key.reversed).replaceFirstChar { it.lowercase() }
        return parts.joinToString(" ")
    }
}
