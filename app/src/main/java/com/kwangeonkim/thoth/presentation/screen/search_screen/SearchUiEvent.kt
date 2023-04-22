package com.kwangeonkim.thoth.presentation.screen.search_screen

sealed class SearchUiEvent {
    object MoveToRecentSearchesScreenEvent : SearchUiEvent()
    data class MoveToBookWebPageEvent(val url: String) : SearchUiEvent()
    object DismissKeyboardEvent: SearchUiEvent()
}