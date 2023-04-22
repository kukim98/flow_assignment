package com.kwangeonkim.thoth.presentation.screen.recent_search_screen

data class RecentSearchState(
    val recentSearchList: List<String>
) {
    constructor() : this(recentSearchList = emptyList())
}