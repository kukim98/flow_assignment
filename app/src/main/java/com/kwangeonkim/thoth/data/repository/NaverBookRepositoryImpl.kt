package com.kwangeonkim.thoth.data.repository

import com.kwangeonkim.thoth.data.local.naver.NaverBookDatabase
import com.kwangeonkim.thoth.data.local.naver.dto.NaverBookSearchTextDto
import com.kwangeonkim.thoth.data.remote.naver.NaverBookService
import com.kwangeonkim.thoth.domain.mapper.NaverBookSearchResultMapper
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.domain.repository.NaverBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class NaverBookRepositoryImpl constructor(
    val naverBookService: NaverBookService,
    val naverBookDatabase: NaverBookDatabase,
    val naverBookSearchResultMapper: NaverBookSearchResultMapper
) : NaverBookRepository {

    // Search parameters
    var text: String = ""
    var limit: Int = 10
    var offset: Int = 1

    // Search result
    private val _naverBooks = MutableStateFlow<List<NaverBook>>(emptyList())

    override fun getBooks(): StateFlow<List<NaverBook>> {
        return _naverBooks
    }

    override suspend fun searchBooks(text: String) {
        // Make network query
        val naverBookSearchResultDto = naverBookService.searchBooks(text, limit, offset)

        // Update search result conditionally
        if (this.text != text) {
            _naverBooks.value = naverBookSearchResultMapper.toUiModel(naverBookSearchResultDto)
        } else {
            _naverBooks.value =
                _naverBooks.value + naverBookSearchResultMapper.toUiModel(naverBookSearchResultDto)
        }

        // Update search parameters
        this.text = text
        this.offset += limit

        // Update LRU local cache
        naverBookDatabase.naverBookSearchDao.insertSearchText(
            NaverBookSearchTextDto(
                text = text,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override fun getTopTenRecentSearchTexts(): Flow<List<String>> {
        return naverBookDatabase.naverBookSearchDao.getAllSearchTexts().map {
            it.map { it.text }
        }
    }
}