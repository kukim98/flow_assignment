package com.kwangeonkim.thoth.presentation.screen.search_screen

import androidx.compose.foundation.lazy.LazyListState
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.presentation.Resource

/**
 * Search Screen state.
 *
 * @property text the current text in the search form field
 * @property lastSearchText the text that the current search is based in; required to make subsequent queries
 * @property lazyListState scroll state of the lazy column
 * @property searchStatus network query status to get search results
 * @property displaySearchStatusInFull true if status should be shown in full
 */
data class SearchState(
    var text: String,
    var lastSearchText: String,
    var lazyListState: LazyListState,
    var searchStatus: Resource<Nothing>,
    var noMoreQuery: Boolean,
    var displaySearchStatusInFull: Boolean
) {

    constructor() : this(
        text = "",
        lastSearchText = "",
        lazyListState = LazyListState(),
        searchStatus = Resource.Success(null),
        noMoreQuery = false,
        displaySearchStatusInFull = true
    )
}