package com.kwangeonkim.thoth.presentation.screen.search_screen

import com.kwangeonkim.thoth.domain.model.NaverBook

sealed class SearchEvent {
    data class SearchTextFormFieldValueChangedEvent(val text: String) : SearchEvent()
    object SearchButtonTappedEvent: SearchEvent()
    data class BookListTileTappedEvent(val book: NaverBook): SearchEvent()
    object RecentSearchesButtonTappedEvent: SearchEvent()
    object ScrollReachedNearBottomEvent: SearchEvent()
}