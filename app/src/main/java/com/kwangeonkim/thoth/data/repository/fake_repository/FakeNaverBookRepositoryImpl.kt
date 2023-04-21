package com.kwangeonkim.thoth.data.repository.fake_repository

import android.util.LruCache
import com.kwangeonkim.thoth.data.local.naver.NaverBookDatabase
import com.kwangeonkim.thoth.data.local.naver.dto.NaverBookSearchTextDto
import com.kwangeonkim.thoth.data.model.NaverBookDto
import com.kwangeonkim.thoth.data.model.NaverBookSearchResultDto
import com.kwangeonkim.thoth.data.remote.naver.NaverBookService
import com.kwangeonkim.thoth.domain.mapper.NaverBookSearchResultMapper
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.domain.repository.NaverBookRepository
import kotlinx.coroutines.flow.*

class FakeNaverBookRepositoryImpl constructor(
    val naverBookSearchResultMapper: NaverBookSearchResultMapper
): NaverBookRepository {

    var text = ""
    val searchTexts = LruCache<String, Nothing?>(10)

    // Search result
    private val _naverBooks = MutableStateFlow<List<NaverBook>>(emptyList())
    val naverBooks get(): StateFlow<List<NaverBook>> = _naverBooks

    override suspend fun searchBooks(text: String) {
        // Mock network query
        val naverBookSearchResultDto = NaverBookSearchResultDto.sample()

        // Conditional Update
        if (this.text != text) {
            _naverBooks.value = naverBookSearchResultMapper.toUiModel(naverBookSearchResultDto)
        } else {
            _naverBooks.value =
                _naverBooks.value + naverBookSearchResultMapper.toUiModel(naverBookSearchResultDto)
        }

        // Mock LRU local cache update
        this.text = text
        searchTexts.put(text, null)
    }

    override fun getTopTenRecentSearchTexts(): Flow<List<String>> {
        return listOf(searchTexts.snapshot().keys.toList()).asFlow()
    }
}