package com.kwangeonkim.thoth.presentation.screen.recent_search_screen

sealed class RecentSearchUiEvent {
    data class MoveBackToRecentSearchesScreenEvent(val text: String) : RecentSearchUiEvent()
}