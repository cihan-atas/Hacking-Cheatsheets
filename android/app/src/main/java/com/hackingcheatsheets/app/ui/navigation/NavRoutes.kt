package com.hackingcheatsheets.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Categories : Screen("categories")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Utilities : Screen("utilities")
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
}
