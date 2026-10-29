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
    @ColumnInfo(name = "content_it")
    val contentIt: String?,
    @ColumnInfo(name = "quick_commands_json")
    val quickCommandsJson: String?,
    val tags: String?
)

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

enum class AppLanguage {
    TR, EN, IT
}
