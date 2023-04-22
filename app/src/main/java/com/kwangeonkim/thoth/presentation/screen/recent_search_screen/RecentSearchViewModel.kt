package com.kwangeonkim.thoth.presentation.screen.recent_search_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwangeonkim.thoth.domain.repository.NaverBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchViewModel @Inject constructor(
    private val naverBookRepository: NaverBookRepository
) : ViewModel() {

    private val _state = mutableStateOf(RecentSearchState())
    val state: State<RecentSearchState> = _state

    private val _eventFlow = MutableSharedFlow<RecentSearchUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _searchJob: Job? = null

    init {
        viewModelScope.launch {
            naverBookRepository.getTopTenRecentSearchTexts().collect {
                _state.value = _state.value.copy(recentSearchList = it)
            }
        }
    }

    fun fireEvent(event: RecentSearchEvent) {
        when (event) {
            is RecentSearchEvent.SearchTextTappedEvent -> onSearchTapped(event.text)
        }
    }

    private fun onSearchTapped(text: String) {
        viewModelScope.launch {
            _eventFlow.emit(
                RecentSearchUiEvent.MoveBackToRecentSearchesScreenEvent(text)
            )
        }
    }
}