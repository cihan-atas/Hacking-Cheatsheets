package com.hackingcheatsheets.app.data

import kotlinx.coroutines.flow.Flow

class CheatsheetRepository(private val dao: CheatsheetDao) {

    fun getAllCheatsheets(): Flow<List<Cheatsheet>> = dao.getAllCheatsheets()

    fun getCheatsheetFlow(id: String): Flow<Cheatsheet?> = dao.getCheatsheetFlow(id)

    suspend fun getCheatsheetById(id: String): Cheatsheet? = dao.getCheatsheetById(id)

    fun getCheatsheetsByCategory(category: String): Flow<List<Cheatsheet>> =
        dao.getCheatsheetsByCategory(category)

    fun getCategorySummaries(): Flow<List<CategorySummary>> = dao.getCategorySummaries()

    fun search(query: String): Flow<List<Cheatsheet>> {
        val sanitized = query.trim()
        return if (sanitized.isEmpty()) {
            dao.getAllCheatsheets()
        } else {
            dao.searchCheatsheets(sanitized)
        }
    }

    fun getFavorites(): Flow<List<Cheatsheet>> = dao.getFavoriteCheatsheets()

    fun isFavorite(id: String): Flow<Boolean> = dao.isFavorite(id)

    suspend fun toggleFavorite(id: String, isFav: Boolean) {
        if (isFav) {
            dao.removeFavorite(id)
        } else {
            dao.addFavorite(Favorite(id))
        }
    }
}
