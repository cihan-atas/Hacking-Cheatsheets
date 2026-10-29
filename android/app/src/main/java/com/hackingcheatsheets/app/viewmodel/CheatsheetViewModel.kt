package com.hackingcheatsheets.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hackingcheatsheets.app.data.AppDatabase
import com.hackingcheatsheets.app.data.AppLanguage
import com.hackingcheatsheets.app.data.CategorySummary
import com.hackingcheatsheets.app.data.Cheatsheet
import com.hackingcheatsheets.app.data.CheatsheetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CheatsheetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CheatsheetRepository

    val currentLanguage = MutableStateFlow(AppLanguage.TR)
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<String?>(null)

    val categories: StateFlow<List<CategorySummary>>
    val allCheatsheets: StateFlow<List<Cheatsheet>>
    val searchResults: StateFlow<List<Cheatsheet>>
    val favorites: StateFlow<List<Cheatsheet>>

    init {
        val database = AppDatabase.getInstance(application)
        repository = CheatsheetRepository(database.cheatsheetDao())

        categories = repository.getCategorySummaries()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allCheatsheets = repository.getAllCheatsheets()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        searchResults = searchQuery.flatMapLatest { query ->
            repository.search(query)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        favorites = repository.getFavorites()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun setLanguage(lang: AppLanguage) {
        currentLanguage.value = lang
    }

    fun toggleLanguage() {
        currentLanguage.value = if (currentLanguage.value == AppLanguage.TR) AppLanguage.EN else AppLanguage.TR
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setCategory(category: String?) {
        selectedCategory.value = category
    }

    fun getCheatsheetFlow(id: String) = repository.getCheatsheetFlow(id)

    fun isFavorite(id: String) = repository.isFavorite(id)

    fun toggleFavorite(id: String, currentFavStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, currentFavStatus)
        }
    }
}
