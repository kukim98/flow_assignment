package com.kwangeonkim.thoth.presentation.screen.search_screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import com.kwangeonkim.thoth.domain.model.NaverBook

data class SearchState(
    val text: String,
    val searchResult: List<NaverBook>,
    val lazyListState: LazyListState
) {

    constructor() : this(text = "", searchResult = emptyList(), lazyListState = LazyListState())
}