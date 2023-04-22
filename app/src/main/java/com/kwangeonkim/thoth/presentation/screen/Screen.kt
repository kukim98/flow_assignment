package com.kwangeonkim.thoth.presentation.screen

sealed class Screen(val route: String) {
    object SearchScreen: Screen("/search")
    object RecentSearchScreen: Screen("/recent")
}