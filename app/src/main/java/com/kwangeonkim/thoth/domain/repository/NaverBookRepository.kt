package com.kwangeonkim.thoth.domain.repository

import com.kwangeonkim.thoth.domain.model.NaverBook
import kotlinx.coroutines.flow.Flow

interface NaverBookRepository {

    suspend fun searchBooks(text: String)

    fun getTopTenRecentSearchTexts(): Flow<List<String>>
}