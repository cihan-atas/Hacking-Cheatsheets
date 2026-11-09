package com.hackingcheatsheets.app.ui.i18n

import com.hackingcheatsheets.app.data.AppLanguage

object AppI18n {

    fun appSubtitle(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "$count ARAÇ • %100 ÇEVRİMDIŞI"
        AppLanguage.DE -> "$count TOOLS • 100% OFFLINE"
        AppLanguage.ES -> "$count HERRAMIENTAS • 100% SIN CONEXIÓN"
        AppLanguage.FR -> "$count OUTILS • 100% HORS LIGNE"
        AppLanguage.IT -> "$count STRUMENTI • 100% OFFLINE"
        AppLanguage.PT -> "$count FERRAMENTAS • 100% OFFLINE"
        AppLanguage.RU -> "$count ИНСТРУМЕНТОВ • 100% ОФФЛАЙН"
        AppLanguage.ZH_CN -> "$count 个工具 • 100% 离线可用"
        AppLanguage.AR -> "$count أداة • 100% بدون إنترنت"
        AppLanguage.EL -> "$count ΕΡΓΑΛΕΙΑ • 100% ΕΚΤΟΣ ΣΥΝΔΕΣΗΣ"
        AppLanguage.NB -> "$count VERKTØY • 100% FRAKOBLET"
        else -> "$count TOOLS • 100% OFFLINE"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Araç, komut, parametre veya CVE ara..."
        AppLanguage.DE -> "Tools, Befehle, Flags oder CVEs suchen..."
        AppLanguage.ES -> "Buscar herramientas, comandos, flags o CVEs..."
        AppLanguage.FR -> "Rechercher outils, commandes, options ou CVEs..."
        AppLanguage.IT -> "Cerca strumenti, comandi, flag o CVE..."
        AppLanguage.PT -> "Pesquisar ferramentas, comandos, flags ou CVEs..."
        AppLanguage.RU -> "Поиск инструментов, команд, флагов или CVE..."
        AppLanguage.ZH_CN -> "搜索工具、命令、参数或 CVE..."
        AppLanguage.AR -> "ابحث عن أدوات، أوامر، معلمات أو CVE..."
        AppLanguage.EL -> "Αναζήτηση εργαλείων, εντολών, σημαιών ή CVE..."
        AppLanguage.NB -> "Søk verktøy, kommandoer, flagg eller CVE..."
        else -> "Search tools, commands, flags, CVEs..."
    }

    fun metricTools(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Siber Araç"
        AppLanguage.DE -> "Cyber Tools"
        AppLanguage.ES -> "Herramientas"
        AppLanguage.FR -> "Outils Cyber"
        AppLanguage.IT -> "Strumenti"
        AppLanguage.PT -> "Ferramentas"
        AppLanguage.RU -> "Инструменты"
        AppLanguage.ZH_CN -> "安全工具"
        AppLanguage.AR -> "أدوات سيبرانية"
        AppLanguage.EL -> "Εργαλεία"
        AppLanguage.NB -> "Verktøy"
        else -> "Cyber Tools"
    }

    fun metricCategories(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Kategori"
        AppLanguage.DE -> "Kategorien"
        AppLanguage.ES -> "Categorías"
        AppLanguage.FR -> "Catégories"
        AppLanguage.IT -> "Categorie"
        AppLanguage.PT -> "Categorias"
        AppLanguage.RU -> "Категории"
        AppLanguage.ZH_CN -> "领域分类"
        AppLanguage.AR -> "الفئات"
        AppLanguage.EL -> "Κατηγορίες"
        AppLanguage.NB -> "Kategorier"
        else -> "Domains"
    }

    fun metricOffline(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Çevrimdışı"
        AppLanguage.DE -> "Offline"
        AppLanguage.ES -> "Sin conexión"
        AppLanguage.FR -> "Hors ligne"
        AppLanguage.IT -> "Offline"
        AppLanguage.PT -> "Offline"
        AppLanguage.RU -> "Оффлайн"
        AppLanguage.ZH_CN -> "离线数据库"
        AppLanguage.AR -> "بدون اتصال"
        AppLanguage.EL -> "Εκτός σύνδεσης"
        AppLanguage.NB -> "Frakoblet"
        else -> "Offline"
    }

    fun categoriesHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "⚡ KATEGORİ REHBERİ"
        AppLanguage.DE -> "⚡ KATEGORIEN DURCHSUCHEN"
        AppLanguage.ES -> "⚡ EXPLORAR CATEGORÍAS"
        AppLanguage.FR -> "⚡ EXPLORER LES CATÉGORIES"
        AppLanguage.IT -> "⚡ ESPLORA CATEGORIE"
        AppLanguage.PT -> "⚡ NAVEGAR POR CATEGORIAS"
        AppLanguage.RU -> "⚡ КАТЕГОРИИ И ОБЛАСТИ"
        AppLanguage.ZH_CN -> "⚡ 浏览安全分类"
        AppLanguage.AR -> "⚡ دليل الفئات"
        AppLanguage.EL -> "⚡ ΠΕΡΙΗΓΗΣΗ ΚΑΤΗΓΟΡΙΩΝ"
        AppLanguage.NB -> "⚡ KATEGORIOVERSIKT"
        else -> "⚡ BROWSE CATEGORIES"
    }

    fun viewAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Tümünü Gör →"
        AppLanguage.DE -> "Alle anzeigen →"
        AppLanguage.ES -> "Ver todo →"
        AppLanguage.FR -> "Tout voir →"
        AppLanguage.IT -> "Vedi tutto →"
        AppLanguage.PT -> "Ver tudo →"
        AppLanguage.RU -> "Все →"
        AppLanguage.ZH_CN -> "查看全部 →"
        AppLanguage.AR -> "عرض الكل ←"
        AppLanguage.EL -> "Όλα →"
        AppLanguage.NB -> "Se alle →"
        else -> "View All →"
    }

    fun featuredHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "🔥 ÖNE ÇIKAN SİBER ARAÇLAR"
        AppLanguage.DE -> "🔥 EMPFOHLENE CYBER-TOOLS"
        AppLanguage.ES -> "🔥 HERRAMIENTAS DESTACADAS"
        AppLanguage.FR -> "🔥 OUTILS VEDETTES"
        AppLanguage.IT -> "🔥 STRUMENTI IN EVIDENZA"
        AppLanguage.PT -> "🔥 FERRAMENTAS EM DESTAQUE"
        AppLanguage.RU -> "🔥 РЕКОМЕНДУЕМЫЕ ИНСТРУМЕНТЫ"
        AppLanguage.ZH_CN -> "🔥 推荐精选安全工具"
        AppLanguage.AR -> "🔥 الأدوات المميزة"
        AppLanguage.EL -> "🔥 ΠΡΟΤΕΙΝΟΜΕΝΑ ΕΡΓΑΛΕΙΑ"
        AppLanguage.NB -> "🔥 UTVALGTE VERKTØY"
        else -> "🔥 FEATURED SECURITY TOOLS"
    }

    fun killChainHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "🎯 SALDIRI AŞAMALARI (KILL CHAIN)"
        AppLanguage.DE -> "🎯 MITRE ATT&CK ANGRIFFSPHASEN"
        AppLanguage.ES -> "🎯 FASES DE ATAQUE (KILL CHAIN)"
        AppLanguage.FR -> "🎯 PHASES D'ATTAQUE (KILL CHAIN)"
        AppLanguage.IT -> "🎯 FASI DI ATTACCO (KILL CHAIN)"
        AppLanguage.PT -> "🎯 FASES DE ATAQUE (KILL CHAIN)"
        AppLanguage.RU -> "🎯 ФАЗЫ АТАКИ (KILL CHAIN)"
        AppLanguage.ZH_CN -> "🎯 MITRE 攻击链阶段"
        AppLanguage.AR -> "🎯 مراحل الهجوم (سلسلة القتل)"
        AppLanguage.EL -> "🎯 ΦΑΣΕΙΣ ΕΠΙΘΕΣΗΣ (KILL CHAIN)"
        AppLanguage.NB -> "🎯 ANGREPSFASER (KILL CHAIN)"
        else -> "🎯 ATTACK KILL CHAIN"
    }

    fun categoriesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "KATEGORİLER"
        AppLanguage.DE -> "KATEGORIEN"
        AppLanguage.ES -> "CATEGORÍAS"
        AppLanguage.FR -> "CATÉGORIES"
        AppLanguage.IT -> "CATEGORIE"
        AppLanguage.PT -> "CATEGORIAS"
        AppLanguage.RU -> "КАТЕГОРИИ"
        AppLanguage.ZH_CN -> "安全分类"
        AppLanguage.AR -> "الفئات"
        AppLanguage.EL -> "ΚΑΤΗΓΟΡΙΕΣ"
        AppLanguage.NB -> "KATEGORIER"
        else -> "CATEGORIES"
    }

    fun categoriesSubtitle(lang: AppLanguage, catCount: Int, toolCount: Int): String = when (lang) {
        AppLanguage.TR -> "$catCount Güvenlik Alanı • $toolCount Araç"
        AppLanguage.DE -> "$catCount Sicherheitsbereiche • $toolCount Tools"
        AppLanguage.ES -> "$catCount Áreas de Seguridad • $toolCount Herramientas"
        AppLanguage.FR -> "$catCount Domaines de Sécurité • $toolCount Outils"
        AppLanguage.IT -> "$catCount Aree di Sicurezza • $toolCount Strumenti"
        AppLanguage.PT -> "$catCount Áreas de Segurança • $toolCount Ferramentas"
        AppLanguage.RU -> "$catCount Областей Безопасности • $toolCount Инструментов"
        AppLanguage.ZH_CN -> "$catCount 个安全领域 • $toolCount 个工具"
        AppLanguage.AR -> "$catCount مجال أمني • $toolCount أداة"
        AppLanguage.EL -> "$catCount Τομείς Ασφάλειας • $toolCount Εργαλεία"
        AppLanguage.NB -> "$catCount Sikkerhetsdomener • $toolCount Verktøy"
        else -> "$catCount Security Domains • $toolCount Tools"
    }

    fun toolsBadge(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "$count ARAÇ"
        AppLanguage.DE -> "$count TOOLS"
        AppLanguage.ES -> "$count HERRAMIENTAS"
        AppLanguage.FR -> "$count OUTILS"
        AppLanguage.IT -> "$count STRUMENTI"
        AppLanguage.PT -> "$count FERRAMENTAS"
        AppLanguage.RU -> "$count ИНСТРУМЕНТОВ"
        AppLanguage.ZH_CN -> "$count 个工具"
        AppLanguage.AR -> "$count أداة"
        AppLanguage.EL -> "$count ΕΡΓΑΛΕΙΑ"
        AppLanguage.NB -> "$count VERKTØY"
        else -> "$count TOOLS"
    }

    fun toolsAndGuides(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "$count araç ve kılavuz"
        AppLanguage.DE -> "$count Tools und Anleitungen"
        AppLanguage.ES -> "$count herramientas y guías"
        AppLanguage.FR -> "$count outils et guides"
        AppLanguage.IT -> "$count strumenti e guide"
        AppLanguage.PT -> "$count ferramentas e guias"
        AppLanguage.RU -> "$count инструментов и руководств"
        AppLanguage.ZH_CN -> "$count 个工具与指南"
        AppLanguage.AR -> "$count أداة ودليل"
        AppLanguage.EL -> "$count εργαλεία και οδηγοί"
        AppLanguage.NB -> "$count verktøy og veiledninger"
        else -> "$count tools and guides"
    }

    fun allChip(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Tümü"
        AppLanguage.DE -> "Alle"
        AppLanguage.ES -> "Todo"
        AppLanguage.FR -> "Tous"
        AppLanguage.IT -> "Tutti"
        AppLanguage.PT -> "Todos"
        AppLanguage.RU -> "Все"
        AppLanguage.ZH_CN -> "全部"
        AppLanguage.AR -> "الكل"
        AppLanguage.EL -> "Όλα"
        AppLanguage.NB -> "Alle"
        else -> "All"
    }

    fun popularSearches(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "⚡ HIZLI ARAMA ÖNERİLERİ"
        AppLanguage.DE -> "⚡ BELIEBTE SUCHBEGRIFFE"
        AppLanguage.ES -> "⚡ BÚSQUEDAS POPULARES"
        AppLanguage.FR -> "⚡ RECHERCHES POPULAIRES"
        AppLanguage.IT -> "⚡ RICERCHE POPOLARI"
        AppLanguage.PT -> "⚡ PESQUISAS POPULARES"
        AppLanguage.RU -> "⚡ ПОПУЛЯРНЫЕ ЗАПРОСЫ"
        AppLanguage.ZH_CN -> "⚡ 热门搜索词"
        AppLanguage.AR -> "⚡ عمليات البحث الشائعة"
        AppLanguage.EL -> "⚡ ΔΗΜΟΦΙΛΕΙΣ ΑΝΑΖΗΤΗΣΕΙΣ"
        AppLanguage.NB -> "⚡ POPULÆRE SØK"
        else -> "⚡ POPULAR SEARCHES"
    }

    fun allCheatsheetsCount(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "Tüm Kılavuzlar ($count)"
        AppLanguage.DE -> "Alle Cheatsheets ($count)"
        AppLanguage.ES -> "Todas las guías ($count)"
        AppLanguage.FR -> "Toutes les antisèches ($count)"
        AppLanguage.IT -> "Tutte le schede ($count)"
        AppLanguage.PT -> "Todas as folhas ($count)"
        AppLanguage.RU -> "Все руководства ($count)"
        AppLanguage.ZH_CN -> "全部备忘单 ($count)"
        AppLanguage.AR -> "جميع الأدلة ($count)"
        AppLanguage.EL -> "Όλοι οι οδηγοί ($count)"
        AppLanguage.NB -> "Alle jukseark ($count)"
        else -> "All Cheatsheets ($count)"
    }

    fun resultsFound(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "$count sonuç bulundu"
        AppLanguage.DE -> "$count Ergebnisse gefunden"
        AppLanguage.ES -> "$count resultados encontrados"
        AppLanguage.FR -> "$count résultats trouvés"
        AppLanguage.IT -> "$count risultati trovati"
        AppLanguage.PT -> "$count resultados encontrados"
        AppLanguage.RU -> "Найдено результатов: $count"
        AppLanguage.ZH_CN -> "找到 $count 个结果"
        AppLanguage.AR -> "تم العثور على $count نتيجة"
        AppLanguage.EL -> "Βρέθηκαν $count αποτελέσματα"
        AppLanguage.NB -> "$count resultater funnet"
        else -> "$count results found"
    }

    fun favoritesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "FAVORİLER"
        AppLanguage.DE -> "FAVORITEN"
        AppLanguage.ES -> "FAVORITOS"
        AppLanguage.FR -> "FAVORIS"
        AppLanguage.IT -> "PREFERITI"
        AppLanguage.PT -> "FAVORITOS"
        AppLanguage.RU -> "ИЗБРАННОЕ"
        AppLanguage.ZH_CN -> "我的收藏"
        AppLanguage.AR -> "المفضلة"
        AppLanguage.EL -> "ΑΓΑΠΗΜΕΝΑ"
        AppLanguage.NB -> "FAVORITTER"
        else -> "FAVORITES"
    }

    fun favoritesSubtitle(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "Hızlı saha erişimi için kaydettiğin araçlar ($count)"
        AppLanguage.DE -> "Ihre gespeicherten Cheatsheets für schnellen Zugriff ($count)"
        AppLanguage.ES -> "Tus hojas de trucos guardadas para acceso rápido ($count)"
        AppLanguage.FR -> "Vos antisèches enregistrées pour un accès rapide ($count)"
        AppLanguage.IT -> "I tuoi appunti salvati per un rapido accesso ($count)"
        AppLanguage.PT -> "Suas folhas salvas para acesso rápido no campo ($count)"
        AppLanguage.RU -> "Сохраненные руководства для быстрого доступа ($count)"
        AppLanguage.ZH_CN -> "保存的安全工具与速查备忘单 ($count)"
        AppLanguage.AR -> "ملخصاتك المحفوظة للوصول السريع ($count)"
        AppLanguage.EL -> "Αποθηκευμένοι οδηγοί για γρήγορη πρόσβαση ($count)"
        AppLanguage.NB -> "Dine lagrede jukseark for rask tilgang ($count)"
        else -> "Your saved cheatsheets for quick field access ($count)"
    }

    fun noFavorites(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Henüz yer imi eklenmedi.\nKılavuz sayfasındaki yer imi simgesine basarak ekleyebilirsin."
        AppLanguage.DE -> "Noch keine Lesezeichen vorhanden.\nTippen Sie auf das Lesezeichensymbol in einer Anleitung."
        AppLanguage.ES -> "Aún no hay marcadores.\nToca el icono de marcador en cualquier guía para guardarla."
        AppLanguage.FR -> "Aucun favori pour l'instant.\nAppuyez sur l'icône de marque-page dans un guide."
        AppLanguage.IT -> "Ancora nessun segnalibro.\nTocca l'icona del segnalibro in qualsiasi guida per salvarla."
        AppLanguage.PT -> "Nenhum favorito salvo ainda.\nToque no ícone de favorito em qualquer guia para salvá-lo."
        AppLanguage.RU -> "Закладок пока нет.\nНажмите значок закладки в любом руководстве, чтобы сохранить."
        AppLanguage.ZH_CN -> "暂无收藏项。\n在任意工具页面中点击书签图标即可收藏。"
        AppLanguage.AR -> "لا توجد إشارات مرجعية حتى الآن.\nاضغط على أيقونة الإشارة المرجعية لحفظ الأداة."
        AppLanguage.EL -> "Δεν υπάρχουν σελιδοδείκτες ακόμα.\nΠατήστε το εικονίδιο σελιδοδείκτη για αποθήκευση."
        AppLanguage.NB -> "Ingen bokmerker ennå.\nTrykk på bokmerkeikonet i en veiledning for å lagre den."
        else -> "No bookmarks yet.\nTap the bookmark icon on any guide to save it."
    }

    fun copied(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Panoya kopyalandı!"
        AppLanguage.DE -> "In die Zwischenablage kopiert!"
        AppLanguage.ES -> "¡Copiado al portapapeles!"
        AppLanguage.FR -> "Copié dans le presse-papiers !"
        AppLanguage.IT -> "Copiato negli appunti!"
        AppLanguage.PT -> "Copiado para a área de transferência!"
        AppLanguage.RU -> "Скопировано в буфер обмена!"
        AppLanguage.ZH_CN -> "已复制到剪贴板！"
        AppLanguage.AR -> "تم النسخ إلى الحافظة!"
        AppLanguage.EL -> "Αντιγράφηκε στο πρόχειρο!"
        AppLanguage.NB -> "Kopiert til utklippstavlen!"
        else -> "Copied to clipboard!"
    }

    fun interactiveBuilder(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "⚡ İNTERAKTİF KOMUT OLUŞTURUCU"
        AppLanguage.DE -> "⚡ INTERAKTIVER BEFEHLSGENERATOR"
        AppLanguage.ES -> "⚡ GENERADOR DE COMANDOS INTERACTIVO"
        AppLanguage.FR -> "⚡ GÉNÉRATEUR DE COMMANDES INTERACTIF"
        AppLanguage.IT -> "⚡ GENERATORE DI COMANDI INTERATTIVO"
        AppLanguage.PT -> "⚡ GERADOR DE COMANDOS INTERATIVO"
        AppLanguage.RU -> "⚡ ИНТЕРАКТИВНЫЙ КОНСТРУКТОР КОМАНД"
        AppLanguage.ZH_CN -> "⚡ 交互式命令生成器"
        AppLanguage.AR -> "⚡ منشئ الأوامر التفاعلي"
        AppLanguage.EL -> "⚡ ΔΙΑΔΡΑΣΤΙΚΟΣ ΔΗΜΙΟΥΡΓΟΣ ΕΝΤΟΛΩΝ"
        AppLanguage.NB -> "⚡ INTERAKTIV KOMMANDOBYGGER"
        else -> "⚡ INTERACTIVE COMMAND BUILDER"
    }

    fun quickUtilitiesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "HIZLI ARAÇLAR"
        AppLanguage.DE -> "SCHNELLE UTILITIES"
        AppLanguage.ES -> "UTILIDADES RÁPIDAS"
        AppLanguage.FR -> "UTILITAIRES RAPIDES"
        AppLanguage.IT -> "STRUMENTI RAPIDI"
        AppLanguage.PT -> "UTILITÁRIOS RÁPIDOS"
        AppLanguage.RU -> "БЫСТРЫЕ УТИЛИТЫ"
        AppLanguage.ZH_CN -> "快捷实用工具"
        AppLanguage.AR -> "الأدوات السريعة"
        AppLanguage.EL -> "ΓΡΗΓΟΡΑ ΕΡΓΑΛΕΙΑ"
        AppLanguage.NB -> "HURTIGVERKTØY"
        else -> "QUICK UTILITIES"
    }

    fun quickUtilitiesSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Saha testleri için çevrimdışı yardımcılar"
        AppLanguage.DE -> "Offline-Dienstprogramme für aktive Sicherheitsüberprüfungen"
        AppLanguage.ES -> "Utilidades sin conexión para pruebas activas"
        AppLanguage.FR -> "Utilitaires hors ligne pour les tests sur le terrain"
        AppLanguage.IT -> "Strumenti offline per verifiche sul campo"
        AppLanguage.PT -> "Utilitários offline para testes em campo"
        AppLanguage.RU -> "Оффлайн утилиты для активного тестирования"
        AppLanguage.ZH_CN -> "用于实战渗透评估的离线辅助工具集"
        AppLanguage.AR -> "أدوات مساعدة دون اتصال بالإنترنت للاختبارات الميدانية"
        AppLanguage.EL -> "Εργαλεία εκτός σύνδεσης για δοκιμές πεδίου"
        AppLanguage.NB -> "Frakoblede verktøy for aktive sikkerhetstester"
        else -> "Offline utilities for active assessments"
    }

    fun utilitiesTabs(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.TR -> listOf("Komut Üretici", "Ağ & Filtre", "Kodlayıcı", "Hash Araçları", "Ters Kabuk")
        AppLanguage.DE -> listOf("Befehlsgenerator", "Netzwerk & Filter", "Encoder", "Hash-Tools", "Reverse Shell")
        AppLanguage.ES -> listOf("Generador de Comandos", "Red y Filtro", "Codificador", "Herramientas Hash", "Reverse Shell")
        AppLanguage.FR -> listOf("Générateur", "Réseau & Filtre", "Encodeur", "Outils Hash", "Reverse Shell")
        AppLanguage.IT -> listOf("Generatore Comandi", "Rete & Filtri", "Codificatore", "Strumenti Hash", "Reverse Shell")
        AppLanguage.PT -> listOf("Gerador de Comandos", "Rede & Filtro", "Codificador", "Ferramentas Hash", "Reverse Shell")
        AppLanguage.RU -> listOf("Конструктор", "Сеть и фильтры", "Кодировщик", "Хэш-утилиты", "Reverse Shell")
        AppLanguage.ZH_CN -> listOf("命令生成器", "网络规则", "编码器", "哈希工具", "反向Shell")
        AppLanguage.AR -> listOf("منشئ الأوامر", "الشبكة والتصفية", "المشفر", "أدوات التجزئة", "Reverse Shell")
        AppLanguage.EL -> listOf("Δημιουργός Εντολών", "Δίκτυο & Φίλτρα", "Κωδικοποιητής", "Εργαλεία Hash", "Reverse Shell")
        AppLanguage.NB -> listOf("Kommandobygger", "Nettverk & Filter", "Koder", "Hash-verktøy", "Reverse Shell")
        else -> listOf("Command Builder", "Network & Filter", "Encoder", "Hash Tools", "Reverse Shell")
    }

    fun quickCommandsHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "⚡ HIZLI KOMUT KOPYALAMA"
        AppLanguage.DE -> "⚡ SCHNELLE BEFEHLE"
        AppLanguage.ES -> "⚡ COMANDOS RÁPIDOS"
        AppLanguage.FR -> "⚡ COMMANDES RAPIDES"
        AppLanguage.IT -> "⚡ COMANDI RAPIDI"
        AppLanguage.PT -> "⚡ COMANDOS RÁPIDOS"
        AppLanguage.RU -> "⚡ БЫСТРЫЕ КОМАНДЫ"
        AppLanguage.ZH_CN -> "⚡ 快捷常用命令"
        AppLanguage.AR -> "⚡ أوامر سريعة"
        AppLanguage.EL -> "⚡ ΓΡΗΓΟΡΕΣ ΕΝΤΟΛΕΣ"
        AppLanguage.NB -> "⚡ HURTIGKOMMANDOER"
        else -> "⚡ QUICK COMMANDS"
    }

    fun configureFlagsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Parametreleri ayarla & canlı komut türet"
        AppLanguage.DE -> "Flags anpassen & Live-Befehle generieren"
        AppLanguage.ES -> "Ajustar parámetros y generar comandos en vivo"
        AppLanguage.FR -> "Ajuster les options et générer des commandes en direct"
        AppLanguage.IT -> "Configura i flag e genera comandi in tempo reale"
        AppLanguage.PT -> "Ajustar parâmetros e gerar comandos ao vivo"
        AppLanguage.RU -> "Настройка параметров и генерация команд на лету"
        AppLanguage.ZH_CN -> "调整参数选项并实时生成命令行"
        AppLanguage.AR -> "اضبط المعلمات وقم بتوليد الأوامر مباشرة"
        AppLanguage.EL -> "Ρύθμιση παραμέτρων & άμεση δημιουργία εντολών"
        AppLanguage.NB -> "Juster flagg og generer levende kommandoer"
        else -> "Configure flags & generate live commands"
    }

    fun selectTool(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Araç Seç"
        AppLanguage.DE -> "Tool auswählen"
        AppLanguage.ES -> "Seleccionar Herramienta"
        AppLanguage.FR -> "Sélectionner un Outil"
        AppLanguage.IT -> "Seleziona Strumento"
        AppLanguage.PT -> "Selecionar Ferramenta"
        AppLanguage.RU -> "Выбрать инструмент"
        AppLanguage.ZH_CN -> "选择工具"
        AppLanguage.AR -> "اختر الأداة"
        AppLanguage.EL -> "Επιλογή Εργαλείου"
        AppLanguage.NB -> "Velg verktøy"
        else -> "Select Tool"
    }

    fun searchToolsPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Araç ara (örn: masscan, nikto, wpscan, mimikatz)..."
        AppLanguage.DE -> "Tools suchen (z.B. nmap, nikto, wpscan)..."
        AppLanguage.ES -> "Buscar herramientas (ej. masscan, nikto)..."
        AppLanguage.FR -> "Rechercher des outils (ex: nmap, nikto)..."
        AppLanguage.IT -> "Cerca strumenti (es. masscan, nikto)..."
        AppLanguage.PT -> "Pesquisar ferramentas (ex: masscan, nikto)..."
        AppLanguage.RU -> "Поиск инструментов (напр. masscan, nikto)..."
        AppLanguage.ZH_CN -> "搜索工具 (例如: masscan, nikto, wpscan)..."
        AppLanguage.AR -> "ابحث عن أدوات (مثل: masscan, nikto)..."
        AppLanguage.EL -> "Αναζήτηση εργαλείων (π.χ. masscan, nikto)..."
        AppLanguage.NB -> "Søk etter verktøy (f.eks. masscan, nikto)..."
        else -> "Search tools (e.g. masscan, nikto, wpscan)..."
    }

    fun availableToolsCount(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "MEVCUT ARAÇLAR ($count)"
        AppLanguage.DE -> "VERFÜGBARE WERKZEUGE ($count)"
        AppLanguage.ES -> "HERRAMIENTAS DISPONIBLES ($count)"
        AppLanguage.FR -> "OUTILS DISPONIBLES ($count)"
        AppLanguage.IT -> "STRUMENTI DISPONIBILI ($count)"
        AppLanguage.PT -> "FERRAMENTAS DISPONÍVEIS ($count)"
        AppLanguage.RU -> "ДОСТУПНЫЕ ИНСТРУМЕНТЫ ($count)"
        AppLanguage.ZH_CN -> "可用安全工具 ($count)"
        AppLanguage.AR -> "الأدوات المتاحة ($count)"
        AppLanguage.EL -> "ΔΙΑΘΕΣΙΜΑ ΕΡΓΑΛΕΙΑ ($count)"
        AppLanguage.NB -> "TILGJENGELIGE VERKTØY ($count)"
        else -> "AVAILABLE TOOLS ($count)"
    }

    fun legalDisclaimerHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "YASAL UYARI / SORUMLULUK REDDİ"
        AppLanguage.DE -> "RECHTLICHER HINWEIS / HAFTUNGSAUSSCHLUSS"
        AppLanguage.ES -> "AVISO LEGAL / DESCARGO DE RESPONSABILIDAD"
        AppLanguage.FR -> "AVIS JURIDIQUE / CLAUSE DE NON-RESPONSABILITÉ"
        AppLanguage.IT -> "DISCLAIMER LEGALE / USO ETICO"
        AppLanguage.PT -> "AVISO LEGAL / ISENÇÃO DE RESPONSABILIDADE"
        AppLanguage.RU -> "ЮРИДИЧЕСКОЕ ПРЕДУПРЕЖДЕНИЕ / ОТКАЗ ОТ ОТВЕТСТВЕННОСТИ"
        AppLanguage.ZH_CN -> "法律声明与免责条约"
        AppLanguage.AR -> "إخلاء مسؤولية قانوني / الاستخدام الأخلاقي"
        AppLanguage.EL -> "ΝΟΜΙΚΗ ΑΠΟΠΟΙΗΣΗ ΕΥΘΥΝΗΣ"
        AppLanguage.NB -> "JURIDISK ANSVARSFRASKRIVELSE"
        else -> "LEGAL DISCLAIMER / ETHICAL USE"
    }

    fun legalDisclaimerText(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Bu araç yalnızca yasal izinli sızma testleri, güvenlik araştırmaları ve eğitim amaçlı tasarlanmıştır. Yetkisiz sistemlere karşı yapılan saldırılar kanunen suçtur. Tüm sorumluluk kullanıcıya aittir."
        AppLanguage.DE -> "Dieses Tool dient ausschließlich autorisierten Penetrationstests, Sicherheitsforschungen und Bildungszwecken. Unbefugte Angriffe auf fremde Systeme sind gesetzlich strafbar."
        AppLanguage.ES -> "Esta herramienta está diseñada únicamente para pruebas de penetración autorizadas, investigación de seguridad y fines educativos. Los ataques a sistemas no autorizados son ilegales."
        AppLanguage.FR -> "Cet outil est conçu uniquement pour les tests d'intrusion autorisés, la recherche en sécurité et l'éducation. Les attaques contre des systèmes non autorisés sont illégales."
        AppLanguage.IT -> "Questo strumento è progettato solo per test di penetrazione autorizzati, ricerca sulla sicurezza e scopi educativi. L'uso non autorizzato è punito dalla legge."
        AppLanguage.PT -> "Esta ferramenta foi desenvolvida exclusivamente para testes de invasão autorizados, pesquisa de segurança e fins educacionais. O uso não autorizado é crime."
        AppLanguage.RU -> "Этот инструмент предназначен исключительно для авторизованного тестирования на проникновение, исследований и обучения. Несанкционированные атаки преследуются по закону."
        AppLanguage.ZH_CN -> "本工具仅供已获授权的渗透测试、安全攻防研究及安全教学使用。未经授权对任何系统进行攻击均属违法行为，一切法律责任由使用者自行承担。"
        AppLanguage.AR -> "هذه الأداة مصممة فقط لاختبارات الاختراق المصرح بها والأبحاث الأمنية والتعليم. الاستخدام غير المصرح به ضد أي أنظمة يعد جريمة يعاقب عليها القانون."
        AppLanguage.EL -> "Αυτό το εργαλείο έχει σχεδιαστεί αποκλειστικά για εξουσιοδοτημένες δοκιμές διείσδυσης, έρευνα ασφάλειας και εκπαίδευση. Η μη εξουσιοδοτημένη χρήση διώκεται ποινικά."
        AppLanguage.NB -> "Dette verktøyet er kun ment for autoriserte penetrasjonstester, sikkerhetsforskning og opplæring. Uautoriserte angrep mot systemer er ulovlig."
        else -> "This tool is intended strictly for authorized penetration testing, security research, and educational purposes. Unauthorized targeting of systems is illegal."
    }

    fun liveCommandHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "CANLI KOMUT"
        AppLanguage.DE -> "LIVE-BEFEHL"
        AppLanguage.ES -> "COMANDO EN VIVO"
        AppLanguage.FR -> "COMMANDE EN DIRECT"
        AppLanguage.IT -> "COMANDO IN TEMPO REALE"
        AppLanguage.PT -> "COMANDO AO VIVO"
        AppLanguage.RU -> "ЖИВАЯ КОМАНДА"
        AppLanguage.ZH_CN -> "实时生成命令"
        AppLanguage.AR -> "الأمر المباشر"
        AppLanguage.EL -> "ΖΩΝΤΑΝΗ ΕΝΤΟΛΗ"
        AppLanguage.NB -> "LEVENDE KOMMANDO"
        else -> "LIVE COMMAND"
    }

    fun commandTemplatesHeader(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.TR -> "HAZIR KOMUT ŞABLONLARI ($count Adet)"
        AppLanguage.DE -> "BEFEHLSVORLAGEN ($count Vorlagen)"
        AppLanguage.ES -> "PLANTILLAS DE COMANDOS ($count)"
        AppLanguage.FR -> "MODÈLES DE COMMANDES ($count)"
        AppLanguage.IT -> "MODELLI DI COMANDO ($count)"
        AppLanguage.PT -> "MODELOS DE COMANDOS ($count)"
        AppLanguage.RU -> "ШАБЛОНЫ КОМАНД ($count)"
        AppLanguage.ZH_CN -> "精选命令模板 ($count 条)"
        AppLanguage.AR -> "قوالب الأوامر الجاهزة ($count)"
        AppLanguage.EL -> "ΠΡΟΤΥΠΑ ΕΝΤΟΛΩΝ ($count)"
        AppLanguage.NB -> "KOMMANDOMALER ($count)"
        else -> "COMMAND TEMPLATES ($count)"
    }
}
