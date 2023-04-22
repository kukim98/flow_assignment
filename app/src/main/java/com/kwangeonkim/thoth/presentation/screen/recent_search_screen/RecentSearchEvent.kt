package com.kwangeonkim.thoth.presentation.screen.recent_search_screen

sealed class RecentSearchEvent {
    data class SearchTextTappedEvent(val text: String): RecentSearchEvent()
}