package com.kwangeonkim.thoth.presentation.screen.search_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.domain.repository.NaverBookRepository
import com.kwangeonkim.thoth.presentation.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
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

    val books = naverBookRepository.getBooks()

    fun fireEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchButtonTappedEvent -> onSearchRequestedFromButtonCallback()
            is SearchEvent.RecentSearchesButtonTappedEvent -> onRecentSearchesButtonTappedCallback()
            is SearchEvent.BookListTileTappedEvent -> onBookListTileTappedCallback(event.book)
            is SearchEvent.SearchTextFormFieldValueChangedEvent -> onSearchTextFormUpdateCallback(
                event.text
            )
            is SearchEvent.ScrollReachedNearBottomEvent -> onSearchRequestedFromScrollingCallback()
        }
    }

    /**
     * Callback for when the search button is tapped.
     *
     * This sends a search request to query data for the text in the form field.
     */
    private fun onSearchRequestedFromButtonCallback() {
        // Save search text and set status to loading
        _state.value = _state.value.copy(
            lastSearchText = _state.value.text,
            searchStatus = Resource.Loading
        )

        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            // Reset scroll position
            resetScrollPosition()

            // Dismiss keyboard
            _eventFlow.emit(SearchUiEvent.DismissKeyboardEvent)

            try {
                // Search query
                naverBookRepository.searchBooks(_state.value.text).let {
                    _state.value = _state.value.copy(
                        searchStatus = Resource.Success(null),
                        displaySearchStatusInFull = true,
                        noMoreQuery = it
                    )
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        _state.value = _state.value.copy(
                            searchStatus = Resource.Failure("ERROR: Check your Internet Connection")
                        )
                    }
                    else -> {
                        _state.value = _state.value.copy(
                            searchStatus = Resource.Failure("ERROR: Try again")
                        )
                    }
                }
            }
        }
    }

    /**
     * Callback for when the scroll position reaches near the end.
     *
     * This sends a search request to query more data for the same search text.
     */
    private fun onSearchRequestedFromScrollingCallback() {
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            // Set status to loading
            _state.value = _state.value.copy(
                searchStatus = Resource.Loading,
                displaySearchStatusInFull = false
            )

            try {
                // Search query
                naverBookRepository.searchBooks(_state.value.lastSearchText).let {
                    _state.value = _state.value.copy(
                        searchStatus = Resource.Success(null),
                        noMoreQuery = it
                    )
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        _state.value = _state.value.copy(
                            searchStatus = Resource.Failure("ERROR: Check your Internet Connection")
                        )
                    }
                    else -> {
                        _state.value = _state.value.copy(
                            searchStatus = Resource.Failure("ERROR: Try again")
                        )
                    }
                }
            }
        }
    }

    /**
     * Callback for when the "최근 검색" button is tapped.
     *
     * This sends a UI event request to move to the Recent Search Screen.
     */
    private fun onRecentSearchesButtonTappedCallback() {
        viewModelScope.launch { _eventFlow.emit(SearchUiEvent.MoveToRecentSearchesScreenEvent) }
    }

    /**
     * Callback for when a book list tile is tapped.
     *
     * This sends a UI event request to call the intent to open the web page for the given book.
     */
    private fun onBookListTileTappedCallback(book: NaverBook) {
        viewModelScope.launch { _eventFlow.emit(SearchUiEvent.MoveToBookWebPageEvent(book.link)) }
    }

    /**
     * Callback for when the search text form field changes.
     *
     * This updates the text value in the state.
     */
    private fun onSearchTextFormUpdateCallback(text: String) {
        _state.value = _state.value.copy(text = text)
    }

    /**
     * Programmatically scroll to the top of the list.
     *
     * The function programmatically moves the scroll view of the search UI to the top of the list.
     * The logic is only executed if the search result at the moment is not empty.
     */
    private suspend fun resetScrollPosition() {
        if (books.value.isNotEmpty()) {
            _state.value = _state.value.apply {
                lazyListState.scrollToItem(0)
            }
        }
    }
}