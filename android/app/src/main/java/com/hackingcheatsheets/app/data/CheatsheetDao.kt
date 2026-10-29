package com.hackingcheatsheets.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CheatsheetDao {

    @Query("SELECT * FROM cheatsheets ORDER BY category ASC, title ASC")
    fun getAllCheatsheets(): Flow<List<Cheatsheet>>

    @Query("SELECT * FROM cheatsheets WHERE id = :id LIMIT 1")
    suspend fun getCheatsheetById(id: String): Cheatsheet?

    @Query("SELECT * FROM cheatsheets WHERE id = :id LIMIT 1")
    fun getCheatsheetFlow(id: String): Flow<Cheatsheet?>

    @Query("SELECT * FROM cheatsheets WHERE category = :category ORDER BY title ASC")
    fun getCheatsheetsByCategory(category: String): Flow<List<Cheatsheet>>

    @Query("SELECT category, COUNT(*) as count FROM cheatsheets GROUP BY category ORDER BY category ASC")
    fun getCategorySummaries(): Flow<List<CategorySummary>>

    @Query("""
        SELECT * FROM cheatsheets 
        WHERE title LIKE '%' || :query || '%' 
           OR title_tr LIKE '%' || :query || '%' 
           OR tags LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
           OR content_en LIKE '%' || :query || '%'
           OR content_tr LIKE '%' || :query || '%'
        ORDER BY category ASC, title ASC
        LIMIT 60
    """)
    fun searchCheatsheets(query: String): Flow<List<Cheatsheet>>

    // Favorites
    @Query("SELECT c.* FROM cheatsheets c INNER JOIN favorites f ON c.id = f.cheatsheet_id ORDER BY f.bookmarked_at DESC")
    fun getFavoriteCheatsheets(): Flow<List<Cheatsheet>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE cheatsheet_id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE cheatsheet_id = :id")
    suspend fun removeFavorite(id: String)
}
