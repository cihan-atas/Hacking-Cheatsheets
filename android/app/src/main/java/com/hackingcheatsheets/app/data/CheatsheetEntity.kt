package com.hackingcheatsheets.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cheatsheets",
    indices = [
        Index(value = ["category"]),
        Index(value = ["title"]),
        Index(value = ["title_tr"])
    ]
)
data class Cheatsheet(
    @PrimaryKey
    val id: String,
    val category: String,
    val title: String,
    @ColumnInfo(name = "title_tr")
    val titleTr: String,
    val description: String?,
    @ColumnInfo(name = "description_tr")
    val descriptionTr: String?,
    val icon: String?,
    @ColumnInfo(name = "content_en")
    val contentEn: String,
    @ColumnInfo(name = "content_tr")
    val contentTr: String,
    @ColumnInfo(name = "content_de")
    val contentDe: String? = null,
    @ColumnInfo(name = "content_es")
    val contentEs: String? = null,
    @ColumnInfo(name = "content_fr")
    val contentFr: String? = null,
    @ColumnInfo(name = "content_it")
    val contentIt: String? = null,
    @ColumnInfo(name = "content_pt")
    val contentPt: String? = null,
    @ColumnInfo(name = "content_ru")
    val contentRu: String? = null,
    @ColumnInfo(name = "content_zh_cn")
    val contentZhCn: String? = null,
    @ColumnInfo(name = "content_ar")
    val contentAr: String? = null,
    @ColumnInfo(name = "content_el")
    val contentEl: String? = null,
    @ColumnInfo(name = "content_nb")
    val contentNb: String? = null,
    @ColumnInfo(name = "quick_commands_json")
    val quickCommandsJson: String?,
    val tags: String?
) {
    fun getContent(lang: AppLanguage): String {
        val c = when (lang) {
            AppLanguage.TR -> contentTr
            AppLanguage.DE -> contentDe
            AppLanguage.ES -> contentEs
            AppLanguage.FR -> contentFr
            AppLanguage.IT -> contentIt
            AppLanguage.PT -> contentPt
            AppLanguage.RU -> contentRu
            AppLanguage.ZH_CN -> contentZhCn
            AppLanguage.AR -> contentAr
            AppLanguage.EL -> contentEl
            AppLanguage.NB -> contentNb
            AppLanguage.EN -> contentEn
        }
        return if (!c.isNullOrBlank()) c else contentEn
    }

    fun getDescription(lang: AppLanguage): String {
        if (lang == AppLanguage.TR && !descriptionTr.isNullOrBlank()) return descriptionTr
        if (lang == AppLanguage.EN && !description.isNullOrBlank()) return description
        val content = getContent(lang)
        val bqMatch = Regex(">\\s*\\*\\*?(.*?)\\*\\*?").find(content)
        if (bqMatch != null) {
            val d = bqMatch.groupValues[1].trim()
            if (d.length > 10) return if (d.length > 180) d.take(180) + "..." else d
        }
        val bMatch = Regex("<b>(.*?)</b>").find(content)
        if (bMatch != null) {
            val d = bMatch.groupValues[1].trim()
            if (d.length > 10) return if (d.length > 180) d.take(180) + "..." else d
        }
        return description ?: descriptionTr ?: "Security cheatsheet & command reference"
    }
}

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "cheatsheet_id")
    val cheatsheetId: String,
    @ColumnInfo(name = "bookmarked_at")
    val bookmarkedAt: Long = System.currentTimeMillis()
)

data class CategorySummary(
    val category: String,
    val count: Int
)

enum class AppLanguage(val code: String, val title: String, val flag: String) {
    TR("tr", "Türkçe", "🇹🇷"),
    EN("en", "English", "🇬🇧"),
    DE("de", "Deutsch", "🇩🇪"),
    ES("es", "Español", "🇪🇸"),
    FR("fr", "Français", "🇫🇷"),
    IT("it", "Italiano", "🇮🇹"),
    PT("pt", "Português", "🇵🇹"),
    RU("ru", "Русский", "🇷🇺"),
    ZH_CN("zh-CN", "简体中文", "🇨🇳"),
    AR("ar", "العربية", "🇸🇦"),
    EL("el", "Ελληνικά", "🇬🇷"),
    NB("nb", "Norsk", "🇳🇴")
}
