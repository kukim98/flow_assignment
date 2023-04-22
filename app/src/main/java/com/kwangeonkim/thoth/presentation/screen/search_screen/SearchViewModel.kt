package com.kwangeonkim.thoth.presentation.screen.search_screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.domain.repository.NaverBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val naverBookRepository: NaverBookRepository
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    private val _eventFlow = MutableSharedFlow<SearchUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _searchJob: Job? = null

    init {
        viewModelScope.launch {
            naverBookRepository.getBooks().collect {
                _state.value = _state.value.copy(searchResult = it)
            }
        }
    }

    fun fireEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchButtonTappedEvent -> onSearchRequestedCallback(true)
            is SearchEvent.RecentSearchesButtonTappedEvent -> onRecentSearchesButtonTappedCallback()
            is SearchEvent.BookListTileTappedEvent -> onBookListTileTappedCallback(event.book)
            is SearchEvent.SearchTextFormFieldValueChangedEvent -> onSearchTextFormUpdateCallback(
                event.text
            )
            is SearchEvent.ScrollReachedNearBottomEvent -> onSearchRequestedCallback(false)
        }
    }

    private fun onSearchRequestedCallback(dismissKeyboard: Boolean) {
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            naverBookRepository.searchBooks(_state.value.text)
            if (dismissKeyboard) {
                resetScrollPosition()
                _eventFlow.emit(SearchUiEvent.DismissKeyboardEvent)
            }
        }
    }

    private fun onRecentSearchesButtonTappedCallback() {

    }

    private fun onBookListTileTappedCallback(book: NaverBook) {
        viewModelScope.launch { _eventFlow.emit(SearchUiEvent.MoveToBookWebPageEvent(book.link)) }
    }

    private fun onSearchTextFormUpdateCallback(text: String) {
        _state.value = _state.value.copy(text = text)
    }

    private suspend fun resetScrollPosition() {
        _state.value = _state.value.apply {
            lazyListState.scrollToItem(0)
        }
    }
}