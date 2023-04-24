package com.kwangeonkim.thoth.data.repository

import com.kwangeonkim.thoth.data.local.naver.NaverBookDatabase
import com.kwangeonkim.thoth.data.local.naver.dto.NaverBookSearchTextDto
import com.kwangeonkim.thoth.data.remote.naver.NaverBookService
import com.kwangeonkim.thoth.domain.mapper.NaverBookSearchResultMapper
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.domain.repository.NaverBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NaverBookRepositoryImpl constructor(
    val naverBookService: NaverBookService,
    val naverBookDatabase: NaverBookDatabase,
    val naverBookSearchResultMapper: NaverBookSearchResultMapper
) : NaverBookRepository {

    // Search parameters
    private var text: String = ""
    private var limit: Int = 10
    private var offset: Int = 1

    // Search result
    private val _naverBooks = MutableStateFlow<List<NaverBook>>(emptyList())

    override fun getBooks(): StateFlow<List<NaverBook>> {
        return _naverBooks
    }

    override suspend fun searchBooks(text: String): Boolean {

        return withContext(Dispatchers.IO) {
            // Clear existing search result and parameter if given different text
            if (this@NaverBookRepositoryImpl.text != text) {
                _naverBooks.value = emptyList()
                this@NaverBookRepositoryImpl.offset = 1
            }

            // Make network query
            val naverBookSearchResultDto = naverBookService.searchBooks(text, limit, offset)

            val result = naverBookSearchResultMapper.toUiModel(naverBookSearchResultDto)

            // Update search result conditionally
            if (this@NaverBookRepositoryImpl.text != text) {
                _naverBooks.value = result
            } else {
                _naverBooks.value =
                    (_naverBooks.value ?: emptyList()) + result
            }

            // Update search parameters
            this@NaverBookRepositoryImpl.text = text
            this@NaverBookRepositoryImpl.offset += limit

            // Update LRU local cache
            naverBookDatabase.naverBookSearchDao.insertSearchText(
                NaverBookSearchTextDto(
                    text = text,
                    timestamp = System.currentTimeMillis()
                )
            )

            // true if no more data to query; false otherwise.
            // Offset greater than 1000 is not allowed by the server
            result.isEmpty() || offset > 1000
        }
    }

    override fun getTopTenRecentSearchTexts(): Flow<List<String>> {
        return naverBookDatabase.naverBookSearchDao.getAllSearchTexts().map {
            it.map { it.text }
        }
    }
}